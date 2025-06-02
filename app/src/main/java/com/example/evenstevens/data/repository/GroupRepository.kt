package com.example.evenstevens.data.repository

import com.example.evenstevens.data.dao.GroupDao
import com.example.evenstevens.data.database.GroupEntity
import kotlinx.coroutines.flow.Flow

class GroupRepository(private val groupDao: GroupDao) {

    val allGroups: Flow<List<GroupEntity>> = groupDao.getAllGroups()

    suspend fun insert(group: GroupEntity) {
        groupDao.insert(group)
    }

    suspend fun update(group: GroupEntity) {
        groupDao.update(group)
    }

    suspend fun delete(group: GroupEntity) {
        groupDao.delete(group)
    }
}
