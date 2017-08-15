<template lang="html">
  <div class="w3-container">
    <Kanban :stages="statuses" :blocks="blocks" @update-block="updateBlock">
        <div v-for="item in blocks" :slot="item.id">
            <div v-on:click.stop.prevent="expandTask = !expandTask">
                <strong>id:</strong> {{ item.id }}
            </div>
            <div>
                {{ item.title }}
            </div>
        </div>
    </Kanban>
    <div v-on:click.stop.prevent="showAddTask = !showAddTask" class="create-new w3-button w3-theme-l1 addTaskButton">Add Task...</div>
    <div v-show="showAddTask" class="addTask">
      <textarea class="w3-input" v-model="newItem.title" type="text" placeholder="Write your task summary..." maxlength="100"></textarea>
      <div class="create-new w3-button w3-round w3-theme-l1 addTaskButton" v-on:click="addTask(newItem,blocks.length + 1)">Save</div>
      <svg class="Cancel" v-on:click="cancelNewTask(list_id)">
	<line x1="0" y1="0" x2="15" y2="15"/>
	<line x1="0" y1="15" x2="15" y2="0"/>
      </svg>
    </div>
  </div>
</template>

<script>
import Kanban from './Kanban'
import Modal from 'modal-vue'
export default {
  name: 'app',
  components: {
    Kanban, Modal
  },
  data () {
    return {
      statuses: ['New', 'In-Progress', 'Done'],
      blocks: [],
      'showAddTask': false,
      'expandTask': false,
      newItem: {}
    }
  },
  mounted () {
   // TODO Need to fetch the details for that initiative from backend here
   // And push them into the blocks array.
  },
  methods: {
    updateBlock (id, status) {
      this.blocks.find(b => b.id === Number(id)).status = status
    },
    addTask (newtask, id) {
      this.blocks.push({
        id: id,
        status: this.statuses[0],
        title: newtask.title
      })
      this.showAddTask = false
      this.newItem.title = ''
      // TODO Send update to backend
    },
    editTask (elem) {
      // this.expandTask = true
    }
  }
}
</script>

<style lang="css">
    $on-hold: #FB7D44;
    $in-progress: #2A92BF;
    $needs-review: #F4CE46;
    $approved: #00B961;
    * {
    	box-sizing: border-box;
    }
    .drag-column {
        &-on-hold {
            .drag-column-header,
            .is-moved,
            .drag-options {
                background: $on-hold;
            }
        }
        &-in-progress {
            .drag-column-header,
            .is-moved,
            .drag-options {
                background: $in-progress;
            }
        }
        &-needs-review {
            .drag-column-header,
            .is-moved,
            .drag-options{
                background: $needs-review;
            }
        }
        &-approved {
            .drag-column-header,
            .is-moved,
            .drag-options {
                background: $approved;
            }
        }
    }
    .section {
    	padding: 20px;
    	text-align: center;
    	a {
    		color: white;
    		text-decoration: none;
    		font-weight: 300;
    	}
    	h4 {
    		font-weight: 400;
    		a {
    			font-weight: 600;
    		}
    	}
    }
$ease-out: all .3s cubic-bezier(0.23, 1, 0.32, 1);

ul.drag-list, ul.drag-inner-list {
    list-style-type: none;
    margin: 0;
    padding: 0;
    style: none;
}

.drag-container {
    max-width: 1000px;
    margin: 20px auto;
}

.drag-list {
    display: flex;
    align-items: flex-start;

    @media (max-width: 690px) {
        display: block;
    }
}

.drag-column {
    flex: 1;
    margin: 0 10px;
    position: relative;
    background: rgba(black, 0.2);
    overflow: hidden;
    background-color: #334191;

    @media (max-width: 690px) {
        margin-bottom: 30px;
    }

    h2 {
        font-size: 0.8rem;
        margin: 0;
        text-transform: uppercase;
        font-weight: 600;
    }
}

.drag-column-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 10px;
    background-color: #334191;
    color: white;
}

.drag-inner-list {
    min-height: 50px;
    background-color: #b1b8e3;
}

.drag-item {
    padding: 10px;
    margin: 10px;
    height: 60px;
    background-color: #4251b066;
    transition: $ease-out;
    list-style: none;
    &.is-moving {
        transform: scale(1.5);
        background: rgba(black, 0.8);
    }

}

.drag-header-more {
    cursor: pointer;
}

.drag-options {
    position: absolute;
    top: 44px;
    left: 0;
    width: 100%;
    height: 100%;
    padding: 10px;
    transform: translateX(100%);
    opacity: 0;
    transition: $ease-out;

    &.active {
        transform: translateX(0);
        opacity: 1;
    }

    &-label {
        display: block;
        margin: 0 0 5px 0;

        input {
            opacity: 0.6;
        }

        span {
            display: inline-block;
            font-size: 0.9rem;
            font-weight: 400;
            margin-left: 5px;
        }
    }
}

/* Dragula CSS  */

.gu-mirror {
  position: fixed !important;
  margin: 0 !important;
  z-index: 9999 !important;
  opacity: 0.8;
    list-style-type: none;
}

.gu-hide {
  display: none !important;
}

.gu-unselectable {
  -webkit-user-select: none !important;
  -moz-user-select: none !important;
  -ms-user-select: none !important;
  user-select: none !important;
}

.gu-transit {
  opacity: 0.2;
}

.addTaskButton {
  display: block;
}

.app-modal{
  z-index: 10;
  display: inline-block;
  color: red;
  background-color: green;
}
.overlay {
  position: fixed;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
  background: rgba(0, 0, 0, 0.7);
  transition: opacity 500ms;
  visibility: hidden;
  opacity: 0;
}
.overlay:target {
  visibility: visible;
  opacity: 1;
}

.popup {
  margin: 70px auto;
  padding: 20px;
  background: #fff;
  border-radius: 5px;
  width: 30%;
  position: relative;
  transition: all 5s ease-in-out;
}

.popup h2 {
  margin-top: 0;
  color: #333;
  font-family: Tahoma, Arial, sans-serif;
}
.popup .close {
  position: absolute;
  top: 20px;
  right: 30px;
  transition: all 200ms;
  font-size: 30px;
  font-weight: bold;
  text-decoration: none;
  color: #333;
}
.popup .close:hover {
  color: #06D85F;
}
.popup .content {
  max-height: 30%;
  overflow: auto;
}

@media screen and (max-width: 700px){
  .box{
    width: 70%;
  }
  .popup{
    width: 70%;
  }
}
</style>
