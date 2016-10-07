<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
	<%@ include file="../Common/Base/CommonHead.jsp"%>
	<script type="text/javascript" src="CbtionListPage/CbtionListPage.js"></script>
	<link rel="stylesheet" type="text/css" href="CbtionListPage/CbtionListPage.css" />
	<script type="text/javascript" src="../elements/CbtionList/CbtionList.js"></script>
	<link rel="stylesheet" type="text/css" href="../elements/CbtionList/CbtionList.css"/>
	<script type="text/javascript" src="../elements/FilterElement/FilterElement.js"></script>
	<link rel="stylesheet" type="text/css" href="../elements/FilterElement/FilterElement.css"/>
	<title>CollectiveOne - Search Contributions</title>
</head>

<script type="text/javascript">
	var JSP_projectFilter = "<s:property value="projectFilter" />";
</script>

<%@ include file="../Common/Base/BodyOver.jsp"%>
<div id="content_pane">
</div>
<%@ include file="../Common/Base/BodyBelow.jsp"%>

</html>
