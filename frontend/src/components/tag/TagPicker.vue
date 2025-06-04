<script setup lang="ts">
import {onMounted, ref, Ref} from "vue";
import {TagDto} from "@/client/data.js";

const props = defineProps<{
  tags: TagDto[],
  selectedTags: TagDto[]
}>();

const searchTags: Ref<TagDto[]> = ref([]);
const selectedTag: Ref<TagDto> = ref(props.tags[0]);

onMounted(() => {
  if (props.selectedTags)
    props.selectedTags.forEach(tag => searchTags.value.push(tag));
});

const emit = defineEmits(['tagsUpdate']);

function addTag() {
  if (selectedTag.value && !searchTags.value.some(t => t === selectedTag.value)) {
    searchTags.value.push(selectedTag.value);
    emitTagsUpdate();
  }
}

function removeTag(tag: TagDto) {
  searchTags.value = searchTags.value.filter(t => t !== tag);
  emitTagsUpdate();
}

function emitTagsUpdate() {
  emit('tagsUpdate', {
    tags: searchTags
  });
}

</script>

<template>
  <div class="tag-input-container">
    <select id="tag" v-model="selectedTag" class="search-input">
      <option v-for="(tag,index) in props.tags" :value="tag" :key="index">
        {{ tag.tag }}
      </option>
    </select>
    <button @click="addTag" class="add-tag-btn">+</button>
  </div>

  <div class="active-tags" v-if="searchTags.length > 0">
        <span class="tag" v-for="(tag,index) in searchTags" :key="index">
          {{ tag.tag }}
          <button @click="removeTag(tag)" class="remove-tag">×</button>
        </span>
  </div>
</template>

<style scoped>
.search-input {
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  margin-right: 10px;
  font-size: 14px;
}

.tag-input-container {
  display: inline-block;
  position: relative;
  margin-top: 10px;
}

.add-tag-btn {
  position: absolute;
  right: 5px;
  top: 50%;
  transform: translateY(-50%);
  background: #4CAF50;
  color: white;
  border: none;
  border-radius: 50%;
  width: 24px;
  height: 24px;
  cursor: pointer;
}

.active-tags {
  margin-top: 10px;
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

.remove-tag {
  background: none;
  border: none;
  color: #888;
  cursor: pointer;
  margin-left: 4px;
  padding: 0;
}
</style>