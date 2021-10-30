package ru.barinov.notes.ui

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity

import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.barinov.databinding.MainLayoutBinding


class MainActivity : AppCompatActivity() {
    private lateinit var  binding: MainLayoutBinding
    private val fragmentManager: FragmentManager = supportFragmentManager
    private lateinit var bottomNavigationItemView: BottomNavigationView
    private val LOCAL_REPOSITORY_NAME = "local_repository.bin"

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        binding= MainLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bottomNavigationItemView= binding.navigationBar
    }


}