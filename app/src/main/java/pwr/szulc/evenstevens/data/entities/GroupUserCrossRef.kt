package pwr.szulc.evenstevens.data.entities

import androidx.room.Entity

@Entity(primaryKeys = ["groupId", "userId"])
data class GroupUserCrossRef(
    val groupId: Int,
    val userId: Int
)