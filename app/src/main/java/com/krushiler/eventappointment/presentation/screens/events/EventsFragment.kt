package com.krushiler.eventappointment.presentation.screens.events

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.recyclerview.widget.DiffUtil
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.krushiler.eventappointment.R
import com.krushiler.eventappointment.data.model.Event
import com.krushiler.eventappointment.databinding.FragmentEventsBinding
import com.krushiler.eventappointment.databinding.ItemEventBinding
import com.krushiler.eventappointment.presentation.util.collectFlow
import com.krushiler.eventappointment.presentation.util.findNavControllerById
import com.krushiler.eventappointment.presentation.util.setVisible
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class EventsFragment : Fragment(R.layout.fragment_events) {
    private val viewModel: EventsViewModel by viewModel()
    private val binding by viewBinding(FragmentEventsBinding::bind)

    private val adapter = AsyncListDifferDelegationAdapter(
        object : DiffUtil.ItemCallback<Event>() {
            override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
                return oldItem == newItem
            }
        },
        eventsAdapterDelegate {
            navController.navigate(
                R.id.action_homeFragment_to_eventDetailsFragment,
                args = Bundle().apply { putString("id", it.id) }
            )
        },
    )

    private lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavControllerById(R.id.root_navigation_container)
        initViews()
        initObservers()
    }

    private fun initViews() {
        binding.eventsRv.adapter = adapter
    }

    private fun initObservers() {
        collectFlow(viewModel.errorFlow) {
            Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
        }
        collectFlow(viewModel.loadingState) {
            binding.loadingIndicator.setVisible(it)
        }
        collectFlow(viewModel.eventsState) {
            adapter.items = it
        }
    }
}

fun eventsAdapterDelegate(onItemClicked: (Event) -> Unit) =
    adapterDelegateViewBinding<Event, Event, ItemEventBinding>({ inflater, root ->
        ItemEventBinding.inflate(
            inflater, root, false
        )
    }) {
        binding.root.setOnClickListener {
            onItemClicked(item)
        }
        bind {
            binding.title.text = item.name
            binding.description.text = item.description
            binding.date.text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(item.date)
            binding.owner.text = item.owner
            binding.registeredMessage.setVisible(item.isMember)
        }
    }