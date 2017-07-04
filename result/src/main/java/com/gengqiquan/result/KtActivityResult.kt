package com.gengqiquan.result

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentTransaction
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject


/**
 * Created by gengqiquan on 2017/7/3.
 */

object RxKtResult {

    fun with(activity: Activity): Suilder {
        val s = Suilder()
        if (activity is FragmentActivity) {
            s.bind(activity)
        } else {
            s.bind(activity)
        }
        return s
    }

    fun with(activity: FragmentActivity): Suilder {
        val s = Suilder()
        s.bind(activity)
        return s
    }

    fun with(fragment: android.app.Fragment): Suilder {
        val s = Suilder()
        s.bind(fragment)
        return s
    }

    fun with(fragment: Fragment): Suilder {
        val s = Suilder()
        s.bind(fragment)
        return s
    }


    private fun post(intent: Intent?) {
        if (intent != null) {
            RxActivityResult.subject?.onNext(intent)
        } else {
            RxActivityResult.subject?.onError(Exception("intent is null"))
        }
    }
}

class Suilder {
    internal var isSuperV4: Boolean = false
    internal var data = Bundle()
    internal var appTransaction: android.app.FragmentTransaction? = null
    internal var v4Transaction: FragmentTransaction? = null


    @SuppressLint("CommitTransaction")
    fun bind(t: android.app.Fragment) {
        appTransaction = t.activity
                .fragmentManager
                .beginTransaction()

    }

    @SuppressLint("CommitTransaction")
    fun bind(t: Fragment) {
        isSuperV4 = true
        v4Transaction = t.activity
                .supportFragmentManager
                .beginTransaction()
    }

    @SuppressLint("CommitTransaction")
    fun bind(t: Activity) {

        appTransaction = t.fragmentManager
                .beginTransaction()


    }

    @SuppressLint("CommitTransaction")
    fun bind(t: FragmentActivity) {
        isSuperV4 = true
        v4Transaction = t.supportFragmentManager
                .beginTransaction()

    }

    constructor()

    fun startActivityWithResult(intent: Intent?, vararg params: Pair<String, Any>): Observable<Intent> {
        data.putAll(Bundle().pair(params))
        return startActivityWithResult(intent)
    }

    fun startActivityWithResult(intent: Intent?): Observable<Intent> {
        if (intent == null) {
            throw RuntimeException("intent can not be null")
        }
        RxActivityResult.subject = PublishSubject.create<Intent>()
        intent.putExtras(data)
        val bundle = Bundle()
        bundle.putParcelable("data", intent)
        if (isSuperV4) {
            val v4Fragment = V4Fragment()
            v4Fragment.arguments = bundle
            v4Transaction!!.replace(android.R.id.content, v4Fragment)
                    .commitAllowingStateLoss()
            v4Transaction = null
        } else {
            val appFragment = AppFragment()
            appFragment.arguments = bundle
            appTransaction?.replace(android.R.id.content, appFragment)
                    ?.commitAllowingStateLoss()
            v4Transaction = null
        }

        return RxActivityResult.subject!!
    }


    fun putBoolean(key: String, value: Boolean): Suilder {
        data.putBoolean(key, value)
        return this
    }

    fun putInt(key: String, value: Int): Suilder {
        data.putInt(key, value)
        return this
    }

    fun putLong(key: String, value: Long): Suilder {
        data.putLong(key, value)
        return this
    }

    fun putDouble(key: String, value: Double): Suilder {
        data.putDouble(key, value)
        return this
    }

    fun putString(key: String, value: String): Suilder {
        data.putString(key, value)
        return this
    }

    fun putBundle(key: String, value: Bundle): Suilder {
        data.putBundle(key, value)
        return this
    }
    fun putAll(value: Bundle): Suilder {
        data.putAll( value)
        return this
    }


}
