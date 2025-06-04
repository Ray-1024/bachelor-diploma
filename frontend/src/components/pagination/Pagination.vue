<script setup lang="ts">
import {ref} from 'vue';

const props = defineProps<{
  currentPage: Number,
  totalPages: Number
}>();

const currentPage = ref(1);
const emit = defineEmits(['page-change']);

function goToPage(page: Number) {
  const newPage = Math.max(1, Math.min(page.valueOf(), props.totalPages.valueOf()));
  if (newPage !== currentPage.value) {
    currentPage.value = newPage;
    emit('page-change', {page: currentPage.value});
  }
}
</script>

<template>
  <div class="pagination">
    <button
        @click="goToPage(currentPage - 1)"
        :disabled="currentPage === 1"
        class="pagination-button"
    >
      &lt;
    </button>

    <span class="page-number"> {{ currentPage }} </span>

    <button
        @click="goToPage(currentPage + 1)"
        :disabled="currentPage === totalPages"
        class="pagination-button"
    >
      &gt;
    </button>
  </div>
</template>

<style scoped>
.pagination {
  display: flex;
  align-items: center;
  gap: 1rem;
  font-family: Arial, sans-serif;
}

.pagination-button {
  padding: 0.5rem 1rem;
  border: 1px solid #ddd;
  background-color: #f8f8f8;
  cursor: pointer;
  border-radius: 4px;
  font-size: 1rem;
}

.pagination-button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.pagination-button:not(:disabled):hover {
  background-color: #e8e8e8;
}

.page-number {
  font-weight: bold;
  padding: 0.25rem 0.5rem;
}
</style>