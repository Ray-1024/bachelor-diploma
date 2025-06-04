<script setup>

</script>

<template>
<span class="tag">
  <slot></slot>
</span>
</template>

<style scoped>
span {
  margin-right: 20px;
  margin-top: 10px;
  font-size: 14px;
  color: #666;
}

.tag {
  display: inline-block;
  background: #e0e0e0;
  padding: 4px 8px;
  border-radius: 4px;
  margin-right: 5px;
  margin-bottom: 5px;
  font-size: 12px;
}
</style>