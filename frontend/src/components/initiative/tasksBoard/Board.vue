<template lang="html">
  <div class="w3-container">
    <app-tasks-column v-for="column in board.columns"
      :column="column">
    </app-tasks-column>
  </div>
</template>

<script>
import Column from './Column.vue'

export default {
  components: {
    'app-tasks-column': Column
  },
  data () {
    return {
      board: {
        columns: [
          {
            title: 'title1',
            cards: [
              { text: 'text1' }
            ]
          },
          {
            title: 'title2',
            cards: [
              { text: 'text2' }
            ]
          },
          {
            title: 'title3',
            cards: [
              { text: 'text3' }
            ]
          }
        ]
      }
    }
  }
}
</script>

<style lang="css">
</style>
