package com.priamm.mvisample.presentation.main

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.priamm.mvisample.R
import com.priamm.mvisample.data.network.GitHubDataSource
import com.priamm.mvisample.domain.entity.RepoEntity
import com.priamm.mvisample.domain.repository.GitHubRepositoryImpl
import com.priamm.mvisample.extensions.hideKeyboard
import com.priamm.mvisample.extensions.provideMachine
import com.priamm.mvisample.presentation.common.GenericListAdapter
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.android.synthetic.main.repo_item.view.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.consumeEach
import kotlin.coroutines.CoroutineContext

@ExperimentalCoroutinesApi
class MainFragment : Fragment(), CoroutineScope {

    override val coroutineContext: CoroutineContext = Job() + Dispatchers.Main
    private lateinit var machine: MainMachine

    private val repoListAdapter by lazy {
        GenericListAdapter<ConstraintLayout, RepoEntity>(R.layout.repo_item) { layout, repoItem ->
            layout.repoName.text = repoItem.name
        }
    }

    private val errorSnackbar by lazy {
        Snackbar.make(requireView(), "", Snackbar.LENGTH_LONG)
            .addCallback(object : Snackbar.Callback() {
                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    machine.events.offer(MainMachine.Event.ErrorMessageDismissed)
                }
            })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        machine = provideMachine {
            val initial = savedInstanceState?.getParcelable(SAVED_STATE_KEY) ?: MainMachine.State()
            val gitHubRepository = GitHubRepositoryImpl(GitHubDataSource())
            MainMachine(initial, gitHubRepository)
        }
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        swipeRefreshLayout.setOnRefreshListener {
            machine.events.offer(MainMachine.Event.RefreshClicked)
        }
        repoListView.adapter = repoListAdapter
        launch {
            machine.states.consumeEach(::render)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
        menu.forEach {
            when (it.itemId) {
                R.id.action_search -> {
                    (it.actionView as SearchView).setOnQueryTextListener(
                        object : SearchView.OnQueryTextListener {
                            override fun onQueryTextSubmit(query: String?): Boolean {
                                if (query != null && query.isNotBlank()) {
                                    machine.events.offer(
                                        MainMachine.Event.SearchSubmitted(query.trim())
                                    )
                                }
                                hideKeyboard()
                                return true
                            }

                            override fun onQueryTextChange(newText: String?): Boolean {
                                return false
                            }
                        })
                }
                R.id.action_refresh -> {
                    it.setOnMenuItemClickListener {
                        machine.events.offer(MainMachine.Event.RefreshClicked)
                        true
                    }
                }
            }
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun render(state: MainMachine.State) = state.run {
        if (shouldShowError && !errorSnackbar.isShownOrQueued) {
            errorSnackbar.setText(errorMessage).show()
        }
        swipeRefreshLayout.isRefreshing = isInProgress
        repoListAdapter.items = searchResults
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(SAVED_STATE_KEY, machine.state)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineContext.cancel()
    }

    companion object {
        const val SAVED_STATE_KEY = "main_fragment_saved_state"
    }
}