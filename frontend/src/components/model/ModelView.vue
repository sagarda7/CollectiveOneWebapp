<template lang="html">
  <div class="">

    <div class="slider-container">
      <transition name="slideDownUp">
        <app-model-view-modal
          v-if="showViewModal"
          :isNew="isNew"
          :viewId="view.id"
          :initiativeId="initiativeId"
          :onlyMessages="onlyMessages"
          @close="showViewModal = false">
        </app-model-view-modal>
      </transition>
    </div>

    <div class="slider-container">
      <transition name="slideDownUp">
        <app-model-section-modal
          v-if="showSectionModal"
          :isNew="true"
          :initiativeId="initiativeId"
          :inView="true"
          :inElementId="view.id"
          :inElementTitle="view.title"
          @close="showSectionModal = false">
        </app-model-section-modal>
      </transition>
    </div>

    <div v-if="view" class="model-view-container w3-display-container">
      <div class="view-title"
        @mouseover="showActionButton = true"
        @mouseleave="showActionButton = false">

        <transition name="fadeenter">
          <div v-if="showActionButton" class="w3-display-topright buttons-container gray-1-color" style="width:100px">
            <div @click="expandViewModal()" class="w3-button model-action-button w3-right tooltip">
              <i class="fa fa-expand" aria-hidden="true"></i>
              <span class="tooltiptext gray-1">expand</span>
            </div>

            <div class="w3-button model-action-button gray-1-color w3-right tooltip"
              @click="cardsAsCardsInit = !cardsAsCardsInit">
              <i class="fa" :class="{ 'fa-bars': cardsAsCardsInit, 'fa-th': !cardsAsCardsInit }" aria-hidden="true"></i>
              <span class="tooltiptext gray-1">toggle card/doc view</span>
            </div>
          </div>
        </transition>

        <div class="w3-rest cursor-pointer"
          @click="expandViewModal(true)">
          <div class="w3-row">
            <div class="w3-left">
              <h1 class="">{{ view.title }}</h1>
            </div>
            <div class="w3-left comments-indicator cursor-pointer"
              @click="expandViewModal(true)">
              <app-indicator
                :initiativeId="view.initiativeId"
                contextType="MODEL_VIEW"
                :contextElementId="view.id"
                type="messages">
              </app-indicator>
            </div>
          </div>
          <div v-if="view.description !== ''" class="w3-row w3-padding light-grey">
            <vue-markdown class="marked-text" :source="view.description"></vue-markdown>
          </div>
        </div>
      </div>

      <div class="w3-row">
        <div class="control-btn w3-left w3-tag gray-1 cursor-pointer noselect"
          @click="expandSubSubsecInit = !expandSubSubsecInit">
          <div class="w3-left button-text">
            <small>
              <span v-if="!expandSubSubsecInit">expand all</span>
              <span v-else>collapse all</span>
            </small>
          </div>
        </div>
      </div>

      <div v-for="section in view.sections"
        :key="section.id"
        class="view-sections">

        <div class="section-drop-area"
          @dragover.prevent
          @drop.prevent="sectionDroped(section.id, $event)">
        </div>

        <app-model-section-with-modal
          :basicPreloaded="true"
          :subElementsPreloaded="false"
          :sectionInit="section"
          :inView="true"
          :inElementId="view.id"
          :inElementTitle="view.title"
          :initiativeId="initiativeId"
          :level="0"
          dragType="MOVE_SECTION"
          :cardsAsCardsInit="cardsAsCardsInit"
          :expandInit="expandSubSubsecInit">
        </app-model-section-with-modal>
      </div>

      <div class="view-sections">
        <div class="section-drop-area"
          @dragover.prevent
          @drop.prevent="sectionDroped('', $event)">
        </div>

        <button class="w3-button w3-border gray-1-color" style="width: 100%; text-align: left"
          @click="newSection()">
          <i class="fa fa-plus w3-margin-right" aria-hidden="true"></i>
          add section
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import ModelSectionWithModal from '@/components/model/ModelSectionWithModal.vue'
import ModelViewModal from '@/components/model/modals/ModelViewModal.vue'
import ModelSectionModal from '@/components/model/modals/ModelSectionModal.vue'

export default {
  name: 'app-model-view',

  components: {
    'app-model-view-modal': ModelViewModal,
    'app-model-section-modal': ModelSectionModal,
    'app-model-section-with-modal': ModelSectionWithModal
  },

  data () {
    return {
      view: null,
      showActionButton: false,
      showViewModal: false,
      showSectionModal: false,
      isNew: false,
      cardsAsCardsInit: true,
      expandSubSubsecInit: false
    }
  },

  watch: {
    '$store.state.support.triggerUpdateModel' () {
      this.update()
    }
  },

  props: {
    initiativeId: {
      type: String,
      default: ''
    },
    viewId: {
      type: String,
      default: ''
    }
  },

  computed: {
    isLoggedAnEditor () {
      return this.$store.getters.isLoggedAnEditor
    }
  },

  methods: {
    update () {
      this.axios.get('/1/initiative/' + this.initiativeId + '/model/view/' + this.viewId, {
        params: {
          level: 1
        }
      }).then((response) => {
        this.view = response.data.data
      })
    },
    expandViewModal (onlyMessages) {
      this.isNew = false
      this.onlyMessages = onlyMessages || false
      this.showViewModal = true
    },
    newSection () {
      this.showSectionModal = true
    },
    sectionDroped (onSectionId, event) {
      var dragData = JSON.parse(event.dataTransfer.getData('text/plain'))
      var url = ''

      if (dragData.type === 'MOVE_SECTION') {
        url = '/1/initiative/' + this.initiativeId +
        '/model/view/' + this.view.id +
        '/moveSection/' + dragData.sectionId

        this.axios.put(url, {}, {
          params: {
            onViewSectionId: onSectionId
          }
        }).then((response) => {
          this.$store.commit('triggerUpdateModel')
        })
      }

      if (dragData.type === 'MOVE_SUBSECTION') {
        url = '/1/initiative/' + this.initiativeId +
        '/model/section/' + dragData.fromElementId +
        '/moveSubsection/' + dragData.sectionId

        this.axios.put(url, {}, {
          params: {
            onViewId: this.view.id,
            onSubsectionId: onSectionId
          }
        }).then((response) => {
          this.$store.commit('triggerUpdateModel')
        })
      }
    }
  },

  mounted () {
    this.update()
  }
}
</script>

<style scoped>

.tooltip .tooltiptext {
    top: 100%;
    right: 50%;
}

.view-title .buttons-container {
  padding-top: 15px;
}

.view-title h1 {
  margin-bottom: 0px;
}

.comments-indicator {
  padding-top: 12px;
  padding-left: 16px;
}

.view-sections {
  /*margin-bottom: 30px;*/
}

.control-btn {
  padding: 2px 16px;
}

.control-btn:hover {
  background-color: #dbdee0;
  color: black;
}

.section-drop-area {
  height: 30px;
  width: 100%;
}

</style>
