package com.priamm.mvisample.extensions

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.priamm.mvisample.data.network.model.GitHubRepoSearchResponse
import com.priamm.mvisample.domain.entity.RepoEntity

@Suppress("UNCHECKED_CAST")
inline fun <reified VM : ViewModel> Fragment.provideMachine(
    crossinline provider: () -> VM
) = ViewModelProviders.of(this, object : ViewModelProvider.Factory {
    override fun <T1 : ViewModel> create(aClass: Class<T1>) = provider() as T1
}).get(VM::class.java)

fun Context.hideKeyboard(view: View?) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
}

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

val GitHubRepoSearchResponse.asRepoEntityList: List<RepoEntity>
    get() = items.orEmpty()
        .filterNotNull()
        .filterNot { it.name.isNullOrBlank() || it.htmlUrl.isNullOrBlank() }
        .map { RepoEntity(it.name!!, it.htmlUrl!!.toUri()) }