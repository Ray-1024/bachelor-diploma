<script setup lang="ts">
import {onMounted, Ref, ref} from "vue";
import {TagDto, TagsDto} from "@/client/data";
import TagPicker from "@/components/tag/TagPicker.vue";

const props = defineProps<{
  tags: TagDto[],
  searchText: String,
  searchTags: TagDto[]
}>();


const emit = defineEmits(['search']);

const searchText: Ref<String> = ref('');
const searchTags: Ref<TagDto[]> = ref([]);

onMounted(() => {
  searchText.value = props.searchText || '';
  searchTags.value = props.searchTags || [];
  emitSearch();
});

function emitSearch() {
  emit('search', {
    text: searchText.value,
    tags: searchTags.value
  });
}

async function tagsUpdateHandler(params: TagsDto) {
  searchTags.value = params.tags;
  emitSearch();
}
</script>

<template>
  <div class="search-controls">
    <div class="search-inputs">
      <input
          type="text"
          v-model="searchText"
          placeholder="Search by article title or content..."
          class="search-input"
          @input="emitSearch"
      />
      <TagPicker :tags="props.tags" @tags-update="tagsUpdateHandler" :selected-tags="props.searchTags"/>
    </div>
  </div>
</template>

<style scoped>
.search-controls {
  margin-bottom: 20px;
  background: #f5f5f5;
  padding: 15px;
  border-radius: 8px;
}

.search-inputs {
  margin-bottom: 15px;
}

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