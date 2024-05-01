package com.example.pet_pawtrol

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.pet_pawtrol.Entity.Pets
import com.example.pet_pawtrol.Entity.Users
import com.example.pet_pawtrol.Entity.Veterinars
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {

    @Insert
    fun insertUser(user: Users)

    @Query("SELECT * FROM USERS")
    fun getAllUser(): Flow<List<Users>>

    @Insert
    fun insertPet(pet: Pets)

    @Query("SELECT * FROM PETS where id_user = :id_user")
    fun getPets(id_user: Int): Flow<List<Pets>>

    @Query("SELECT ID FROM pets WHERE id_user = :id and nickname = :nickname and petView = :view and poroda = :poroda")
    fun getPetsId(id : Int, nickname: String, view: String, poroda: String): Int

    @Delete
    fun deletePet(pet: Pets)

    @Insert
    fun insertVeterinar(veterinars: Veterinars)

    @Query("SELECT * FROM veterinars")
    fun getAllVeterinar(): Flow<List<Veterinars>>

    @Query("SELECT * FROM veterinars where specialization = :spec")
    fun getVeterinarToSpec(spec: String): Flow<List<Veterinars>>
}