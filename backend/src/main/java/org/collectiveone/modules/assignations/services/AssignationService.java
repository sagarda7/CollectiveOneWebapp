package org.collectiveone.modules.assignations.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.collectiveone.common.dto.GetResult;
import org.collectiveone.common.dto.PostResult;
import org.collectiveone.modules.assignations.dto.AssignationDto;
import org.collectiveone.modules.assignations.dto.BillDto;
import org.collectiveone.modules.assignations.dto.EvaluationDto;
import org.collectiveone.modules.assignations.dto.EvaluationGradeDto;
import org.collectiveone.modules.assignations.dto.EvaluatorDto;
import org.collectiveone.modules.assignations.dto.ReceiverDto;
import org.collectiveone.modules.assignations.enums.AssignationState;
import org.collectiveone.modules.assignations.enums.AssignationType;
import org.collectiveone.modules.assignations.enums.EvaluationGradeState;
import org.collectiveone.modules.assignations.enums.EvaluationGradeType;
import org.collectiveone.modules.assignations.enums.EvaluatorState;
import org.collectiveone.modules.assignations.enums.ReceiverState;
import org.collectiveone.modules.assignations.evaluationlogic.PeerReviewedAssignation;
import org.collectiveone.modules.assignations.evaluationlogic.PeerReviewedAssignationState;
import org.collectiveone.modules.assignations.model.Assignation;
import org.collectiveone.modules.assignations.model.Bill;
import org.collectiveone.modules.assignations.model.EvaluationGrade;
import org.collectiveone.modules.assignations.model.Evaluator;
import org.collectiveone.modules.assignations.model.Receiver;
import org.collectiveone.modules.assignations.repositories.AssignationRepositoryIf;
import org.collectiveone.modules.assignations.repositories.BillRepositoryIf;
import org.collectiveone.modules.assignations.repositories.EvaluationGradeRepositoryIf;
import org.collectiveone.modules.assignations.repositories.EvaluatorRepositoryIf;
import org.collectiveone.modules.assignations.repositories.ReceiverRepositoryIf;
import org.collectiveone.modules.initiatives.model.Initiative;
import org.collectiveone.modules.initiatives.repositories.InitiativeRepositoryIf;
import org.collectiveone.modules.tokens.model.TokenType;
import org.collectiveone.modules.tokens.services.TokenService;
import org.collectiveone.modules.tokens.services.TokenTransferService;
import org.collectiveone.modules.users.repositories.AppUserRepositoryIf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AssignationService {
	
	private long DAYS_TO_MS = 24L*60L*60L*1000L;
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private TokenTransferService tokenTransferService;
	
	
	@Autowired
	private AppUserRepositoryIf appUserRepository;
	
	@Autowired
	private InitiativeRepositoryIf initiativeRepository;
	
	@Autowired
	private AssignationRepositoryIf assignationRepository;
	
	@Autowired
	private ReceiverRepositoryIf receiverRepository;
	
	@Autowired
	private EvaluatorRepositoryIf evaluatorRepository;
	
	@Autowired
	private EvaluationGradeRepositoryIf evaluationGradeRepository;
	
	@Autowired
	private BillRepositoryIf billRepository;
	
		
	public PostResult createAssignation(UUID initaitiveId, AssignationDto assignationDto) {
		Initiative initiative = initiativeRepository.findById(initaitiveId);
	
		Assignation assignation = new Assignation();
		
		assignation.setType(AssignationType.valueOf(assignationDto.getType()));
		assignation.setMotive(assignationDto.getMotive());
		assignation.setNotes(assignationDto.getNotes());
		assignation.setInitiative(initiative);
		assignation.setState(AssignationState.OPEN);
		assignation.setMinClosureDate(new Timestamp(System.currentTimeMillis()));
		assignation.setMaxClosureDate(new Timestamp(System.currentTimeMillis() + 7L*DAYS_TO_MS));
		assignation.setState(AssignationState.OPEN);
		assignation = assignationRepository.save(assignation);
		
		for(ReceiverDto receiverDto : assignationDto.getReceivers()) {
			Receiver receiver = new Receiver();
			
			receiver.setUser(appUserRepository.findByC1Id(UUID.fromString(receiverDto.getUser().getC1Id())));
			receiver.setAssignation(assignation);
			receiver.setAssignedPercent(receiverDto.getPercent());
			receiver.setState(ReceiverState.PENDING);
			receiver = receiverRepository.save(receiver);
			
			assignation.getReceivers().add(receiver);
		}
		
		for(BillDto billDto : assignationDto.getAssets()) {
			TokenType tokenType = tokenService.getTokenType(UUID.fromString(billDto.getAssetId()));
			Bill bill = new Bill();
			
			bill.setAssignation(assignation);
			bill.setTokenType(tokenType);
			bill.setValue(billDto.getValue());
			bill = billRepository.save(bill);
			
			assignation.getBills().add(bill);
		}
		
		switch (assignation.getType()) {
		
		case DIRECT:
			for(Bill bill : assignation.getBills()) {
				for(Receiver receiver : assignation.getReceivers()) {
					double value = bill.getValue() * receiver.getAssignedPercent() / 100.0;
					tokenTransferService.transferFromInitiativeToUser(assignation.getInitiative().getId(), receiver.getUser().getC1Id(), bill.getTokenType().getId(), value);
					receiver.setState(ReceiverState.RECEIVED);
				}
			}
			assignation.setState(AssignationState.DONE);
			assignationRepository.save(assignation);
			break;
		
		case PEER_REVIEWED: 
			for(EvaluatorDto evaluatorDto : assignationDto.getEvaluators()) {
				Evaluator evaluator = new Evaluator();
				
				evaluator.setUser(appUserRepository.findByC1Id(UUID.fromString(evaluatorDto.getUser().getC1Id())));
				evaluator.setAssignation(assignation);
				evaluator.setState(EvaluatorState.PENDING);
				/* Weight logic TBD */
				evaluator.setWeight(1.0);
				evaluator = evaluatorRepository.save(evaluator);
				
				/* Create the grades of all evaluators already */
				for(Receiver receiver : assignation.getReceivers()) {
					EvaluationGrade grade = new EvaluationGrade();
					
					grade.setAssignation(assignation);
					grade.setEvaluator(evaluator);
					grade.setReceiver(receiver);
					grade.setPercent(0.0);
					grade.setType(EvaluationGradeType.SET);
					grade.setState(EvaluationGradeState.PENDING);
					grade = evaluationGradeRepository.save(grade);
					
					evaluator.getGrades().add(grade);
				}
				
				assignation.getEvaluators().add(evaluator);
			}
			break;
		}
		
		return new PostResult("success", "success", "");
	}
	
	@Transactional
	private PostResult evaluateAssignation(UUID evaluatorUserId, UUID assignationId, EvaluationDto evaluationsDto) {
		
		Assignation assignation = assignationRepository.findById(assignationId);
		Evaluator evaluator = evaluatorRepository.findByAssignationIdAndUser_C1Id(assignation.getId(), evaluatorUserId);
		
		for(EvaluationGradeDto evaluationGradeDto : evaluationsDto.getEvaluationGrades()) {
			UUID receiverUserId = UUID.fromString(evaluationGradeDto.getReceiverUser().getC1Id());
			EvaluationGrade grade = evaluationGradeRepository.findByAssignation_IdAndReceiver_User_C1IdAndEvaluator_User_C1Id(assignation.getId(), receiverUserId, evaluatorUserId);
					
			grade.setPercent(evaluationGradeDto.getPercent());
			grade.setType(EvaluationGradeType.valueOf(evaluationGradeDto.getType()));
			grade.setState(EvaluationGradeState.DONE);
			
			evaluationGradeRepository.save(grade);			
		}
		
		evaluator.setState(EvaluatorState.DONE);
		evaluatorRepository.save(evaluator);
		
		return new PostResult("success", "evaluation saved", evaluator.getId().toString());
	}

	@Transactional
	public void updateAssignationState(UUID assignationId) {
		
		Assignation assignation = assignationRepository.findById(assignationId);
		
		PeerReviewedAssignation peerReviewedAssignation = new PeerReviewedAssignation();
		peerReviewedAssignation.setAssignation(assignation);
		
		peerReviewedAssignation.updateState();
		
		if(peerReviewedAssignation.getState() == PeerReviewedAssignationState.CLOSED) {
			/* percents already updated by peerReviewAssignation,
			 * so just change state, transfer funds and save */
			
			/* transfer the assets to the receivers */
			for(Bill bill : assignation.getBills()) {
				for(Receiver receiver : assignation.getReceivers()) {
					double value = bill.getValue() * receiver.getAssignedPercent() / 100.0;
					tokenTransferService.transferFromInitiativeToUser(assignation.getInitiative().getId(), receiver.getUser().getC1Id(), bill.getTokenType().getId(), value);
					receiver.setState(ReceiverState.RECEIVED);
				}
			}
			
			assignation.setState(AssignationState.DONE);
			assignationRepository.save(assignation);
		}
	}
	
	@Transactional
	public AssignationDto getPeerReviewedAssignation(UUID initiativeId, UUID assignationId, UUID evaluatorAppUserId) {
		Assignation assignation = assignationRepository.findById(assignationId);
		AssignationDto assignationDto = assignation.toDto();
		
		/* add the evaluations of logged user */
		Evaluator evaluator = evaluatorRepository.findByAssignationIdAndUser_C1Id(assignation.getId(), evaluatorAppUserId);
		
		EvaluationDto evaluation = new EvaluationDto();
		evaluation.setId(evaluator.getGrades().toString());
		evaluation.setEvaluationState(evaluator.getState().toString());
		
		for (EvaluationGrade grade : evaluator.getGrades()) {
			evaluation.getEvaluationGrades().add(grade.toDto());
		}
		
		assignationDto.setThisEvaluation(evaluation);
		return assignationDto;
	}
	
	@Transactional
	public GetResult<List<AssignationDto>> getAssignations(UUID initiativeId, UUID evaluatorAppUserId) {
		List<Assignation> assignations = assignationRepository.findByInitiativeId(initiativeId);
		List<AssignationDto> assignationsDtos = new ArrayList<AssignationDto>();
		
		for(Assignation assignation : assignations) {
			if(assignation.getType() == AssignationType.PEER_REVIEWED) {
				assignationsDtos.add(getPeerReviewedAssignation(initiativeId, assignation.getId(), evaluatorAppUserId));
			} else {
				assignationsDtos.add(assignation.toDto());
			}
		}
		
		return new GetResult<List<AssignationDto>>("success", "assignations found", assignationsDtos);
	}
	
	/** Non-transactional to evaluate and update assignation state in different transactions */
	public PostResult evaluateAndUpdateAssignation(UUID evaluatorAppUserId, UUID assignationId, EvaluationDto evaluationDto) {
		PostResult result = evaluateAssignation(evaluatorAppUserId, assignationId, evaluationDto);
		updateAssignationState(assignationId);
		
		return result;
	}
}