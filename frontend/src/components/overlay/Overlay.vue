<template>
  <div>
    <div @click="showModal=true">
      <slot name="card"></slot>
    </div>
    <div v-if="showModal" class="modal-overlay" @click.self="showModal = false">
      <div class="modal-container">
        <div class="modal">
          <div class="modal-actions">
            <button @click="showModal = false" class="close-btn">Close</button>
          </div>
          <div class="modal-content">
            <slot name="popup"></slot>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import {ref} from "vue";

const showModal = ref(false);

</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
  overflow-y: auto;
  padding: 20px;
}

.modal-container {
  width: 100%;
  max-height: 90vh;
  display: flex;
  justify-content: center;
}

.modal {
  background: white;
  border-radius: 8px;
  width: 100%;
  display: flex;
  flex-direction: column;
  max-height: 100%;
}

.modal-content {
  padding: 30px;
  overflow-y: auto;
}

.modal-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  padding: 20px 20px 0 20px;
}

.close-btn {
  background-color: #95a5a6;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
}
</style>