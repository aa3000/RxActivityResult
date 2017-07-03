package com.gengqiquan.rxactivityresult

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.gengqiquan.result.RxResult
import kotlinx.android.synthetic.main.activity_main2.*

class SecondActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        Toast.makeText(this@SecondActivity, intent.getStringExtra("key"), Toast.LENGTH_SHORT).show()

        button.setOnClickListener {
            setResult(Activity.RESULT_OK, Intent().putExtra("msg", "日落下山红霞飞"))
            finish()
        }
        jump.setOnClickListener {
            RxResult.with(this).putString("key", "哭一个")
                    .start(Intent(this@SecondActivity, ThirdActivity::class.java)).subscribe({ intent ->
                Toast.makeText(this@SecondActivity, intent.getStringExtra("msg"), Toast.LENGTH_SHORT).show()
            },
                    {
                        e ->
                        e.printStackTrace()
                    })
        }
    }
}