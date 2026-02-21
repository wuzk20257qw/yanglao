<template>
  <div>
    <form @submit.prevent="create" style="display:flex;gap:8px;align-items:center">
      <input v-model="form.name" placeholder="姓名" required />
      <input v-model.number="form.age" type="number" placeholder="年龄" required style="width:80px;" />
      <input v-model="form.phone" placeholder="电话" style="width:160px;" />
      <button type="submit">添加</button>
    </form>

    <div style="margin:12px 0;display:flex;gap:8px;align-items:center">
      <input v-model="searchName" placeholder="按姓名搜索" style="width:200px" @keyup.enter="onSearch" />
      <button @click="onSearch">搜索</button>
      <button @click="clearSearch">清除</button>
      <div style="margin-left:auto">第 {{ currentPage + 1 }} 页 / 共 {{ totalPages }} 页</div>
    </div>

    <hr />

    <table border="1" cellpadding="6" style="width:100%;border-collapse:collapse">
      <thead>
        <tr><th>ID</th><th>姓名</th><th>年龄</th><th>电话</th><th>操作</th></tr>
      </thead>
      <tbody>
        <tr v-for="e in elders" :key="e.id">
          <td>{{ e.id }}</td>
          <td>{{ e.name }}</td>
          <td>{{ e.age }}</td>
          <td>{{ e.phone }}</td>
          <td style="display:flex;gap:6px">
            <button @click="edit(e)">编辑</button>
            <button @click="remove(e.id)">删除</button>
          </td>
        </tr>
      </tbody>
    </table>

    <div style="margin-top:8px;display:flex;gap:8px;align-items:center">
      <button :disabled="currentPage<=0" @click="goto(currentPage-1)">上一页</button>
      <button :disabled="currentPage+1>=totalPages" @click="goto(currentPage+1)">下一页</button>
      <select v-model.number="pageSize" @change="onPageSizeChange">
        <option :value="5">5</option>
        <option :value="10">10</option>
        <option :value="20">20</option>
      </select>
      <div style="margin-left:auto">总条数: {{ totalElements }}</div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

const elders = ref([])
const form = ref({ name: '', age: 70, phone: '' })
const searchName = ref('')
const currentPage = ref(0)
const pageSize = ref(10)
const totalPages = ref(1)
const totalElements = ref(0)

const fetchList = async () => {
  const params = new URLSearchParams()
  if (searchName.value) params.append('name', searchName.value)
  params.append('page', String(currentPage.value))
  params.append('size', String(pageSize.value))
  const res = await axios.get('/api/elders?' + params.toString())
  // support both legacy array and Page response
  if (Array.isArray(res.data)) {
    elders.value = res.data
    totalPages.value = 1
    totalElements.value = res.data.length
  } else if (res.data.content) {
    elders.value = res.data.content
    totalPages.value = res.data.totalPages || 1
    totalElements.value = res.data.totalElements || res.data.content.length
  } else {
    elders.value = res.data
    totalPages.value = 1
    totalElements.value = res.data.length
  }
}

const create = async () => {
  await axios.post('/api/elders', form.value)
  form.value = { name: '', age: 70, phone: '' }
  currentPage.value = 0
  await fetchList()
}

const remove = async (id) => {
  if (!confirm('确认删除该条记录？')) return
  await axios.delete(`/api/elders/${id}`)
  await fetchList()
}

const edit = async (e) => {
  const newName = prompt('姓名', e.name)
  if (newName === null) return
  const newPhone = prompt('电话', e.phone || '')
  if (newPhone === null) return
  const newAgeStr = prompt('年龄', e.age || 70)
  if (newAgeStr === null) return
  const newAge = Number(newAgeStr) || e.age
  await axios.put(`/api/elders/${e.id}`, { ...e, name: newName, phone: newPhone, age: newAge })
  await fetchList()
}

const onSearch = async () => {
  currentPage.value = 0
  await fetchList()
}

const clearSearch = async () => {
  searchName.value = ''
  currentPage.value = 0
  await fetchList()
}

const goto = async (p) => {
  currentPage.value = p
  await fetchList()
}

const onPageSizeChange = async () => {
  currentPage.value = 0
  await fetchList()
}

onMounted(fetchList)
</script>
