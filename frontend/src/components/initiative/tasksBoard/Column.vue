<template lang="html">
  <div class="w3-panel w3-theme w3-padding w3-round column-div">
    <h4>{{ column.title }}</h4>
    <app-task-card></app-task-card>
  </div>
</template>

<script>
import Card from './Card.vue'

export default {
  props: {
    column: {
      type: Object
    }
  },
  components: {
    'app-task-card': Card
  }
}
</script>

<style scoped>

.column-div {
  width: 300px;
  padding-bottom: 20px !important;
}

</style>
