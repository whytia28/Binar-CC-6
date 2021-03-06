package com.example.binarchapter6.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.binarchapter6.fragment.MemoFragment
import com.example.binarchapter6.R
import com.example.binarchapter6.adapter.AdapterMemo
import com.example.binarchapter6.database.MemoDatabase
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ProfileActivity : AppCompatActivity(), View.OnClickListener {

    private var memoDb: MemoDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        showProfile()
        fetchMemo()
        btn_back.setOnClickListener(this)
        fab_add.setOnClickListener(this)

        memoDb = MemoDatabase.getInstance(this)

        rv_memo.layoutManager = LinearLayoutManager(this)
        rv_memo.setHasFixedSize(true)

    }

    override fun onResume() {
        super.onResume()
        fetchMemo()
    }

    private fun showProfile() {
        val sharedPreferences = getSharedPreferences("suitGame", Context.MODE_PRIVATE)
        val email = sharedPreferences.getString("email", "Email")
        val name = sharedPreferences.getString("name", "Name")

        et_email.setText(email)
        et_name.setText(name)
    }

    fun fetchMemo() {

        GlobalScope.launch {
            val listMemo = memoDb?.memoDao()?.getAllMemo()
            runOnUiThread {
                listMemo?.let {
                    val adapter =
                        AdapterMemo(it)
                    rv_memo.adapter = adapter
                }
            }
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_back -> {
                onBackPressed()
            }
            R.id.fab_add -> {
                val pop = MemoFragment()
                    .newInstance()
                val fragmentManager = supportFragmentManager
                pop.show(fragmentManager, "fragment_dialog")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        MemoDatabase.destroyInstance()
    }
}