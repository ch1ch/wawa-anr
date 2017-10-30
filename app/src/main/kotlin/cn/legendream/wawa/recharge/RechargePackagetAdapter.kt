package cn.legendream.wawa.recharge

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.legendream.wawa.R
import cn.legendream.wawa.app.model.GameCoinPackage
import kotlinx.android.synthetic.main.lay_package_item.view.*

/**
 * Created by zhao on 2017/10/30.
 */


class RechargePackagetAdapter : RecyclerView.Adapter<RechargePackagetAdapter.RechargeItemViewHolder>() {

    private var rechargePackageList: List<GameCoinPackage>? = null
    private var onClickPackageListener: OnClickRechargePackageListener? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RechargeItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.lay_package_item, parent,
            false)
        return RechargeItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: RechargeItemViewHolder, position: Int) {
        val coilPackage = rechargePackageList?.get(position)
        coilPackage?.let {
            holder.itemView.tv_package_name.text = coilPackage.packageName
            holder.itemView.setOnClickListener {
                onClickPackageListener?.onClickPackage(coilPackage)
            }
        }
    }

    override fun getItemCount(): Int {
        return rechargePackageList?.size ?: 0
    }

    fun setRechargePackageList(rechargePackageList: List<GameCoinPackage>) {
        this.rechargePackageList = rechargePackageList
        notifyDataSetChanged()
    }

    fun setOnClickRechargePackageListener(onClickPackageListener: OnClickRechargePackageListener) {
        this.onClickPackageListener = onClickPackageListener
    }

    interface OnClickRechargePackageListener {
        fun onClickPackage(gameCoinPackage: GameCoinPackage)
    }

    class RechargeItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}