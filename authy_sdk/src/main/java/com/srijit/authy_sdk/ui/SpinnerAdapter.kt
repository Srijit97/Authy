package com.srijit.authy_sdk.ui

import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.skydoves.powerspinner.IconSpinnerItem
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener
import com.skydoves.powerspinner.PowerSpinnerInterface
import com.skydoves.powerspinner.PowerSpinnerView
import com.srijit.authy_sdk.databinding.LayoutSpinnerItemBinding

class SpinnerAdapter(private val powerSpinnerView: PowerSpinnerView) :
    RecyclerView.Adapter<SpinnerAdapter.IconSpinnerViewHolder>(),
    PowerSpinnerInterface<IconSpinnerItem> {

    private val spinnerItems: MutableList<IconSpinnerItem> = arrayListOf()

    override var index: Int = powerSpinnerView.selectedIndex

    override var onSpinnerItemSelectedListener: OnSpinnerItemSelectedListener<IconSpinnerItem>? =
        null
    override val spinnerView: PowerSpinnerView
        get() = powerSpinnerView

    override fun getItemCount(): Int {
        return spinnerItems.size
    }

    override fun notifyItemSelected(index: Int) {
        if (index == -1) return
        val item = spinnerItems[index]
        spinnerView.compoundDrawablePadding =
            item.iconPadding ?: spinnerView.compoundDrawablePadding
        val icon = item.iconRes?.let {
            ResourcesCompat.getDrawable(spinnerView.resources, it, null)
        } ?: item.icon
        when (item.iconGravity) {
            Gravity.START ->
                spinnerView.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null)
            Gravity.END ->
                spinnerView.setCompoundDrawablesWithIntrinsicBounds(null, null, icon, null)
            Gravity.TOP ->
                spinnerView.setCompoundDrawablesWithIntrinsicBounds(null, icon, null, null)
            Gravity.BOTTOM ->
                spinnerView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, icon)
        }
        val oldIndex = this.index
        this.index = index
        this.spinnerView.notifyItemSelected(index, item.text)
        this.onSpinnerItemSelectedListener?.onItemSelected(
            oldIndex = oldIndex,
            oldItem = oldIndex.takeIf { it != -1 }?.let { spinnerItems[oldIndex] },
            newIndex = index,
            newItem = item
        )
    }

    override fun setItems(itemList: List<IconSpinnerItem>) {
        spinnerItems.clear()
        spinnerItems.addAll(itemList)
        index = -1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IconSpinnerViewHolder {
        val binding =
            LayoutSpinnerItemBinding.inflate(
                LayoutInflater.from(parent.context), parent,
                false
            )
        return IconSpinnerViewHolder(binding).apply {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition.takeIf { it != RecyclerView.NO_POSITION }
                    ?: return@setOnClickListener
                notifyItemSelected(position)
            }
        }
    }

    override fun onBindViewHolder(holder: IconSpinnerViewHolder, position: Int) {
        holder.bind(spinnerItems[position], spinnerView)
    }

    inner class IconSpinnerViewHolder(private val binding: LayoutSpinnerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: IconSpinnerItem, spinnerView: PowerSpinnerView) {
            binding.data = item
        }

    }
}
