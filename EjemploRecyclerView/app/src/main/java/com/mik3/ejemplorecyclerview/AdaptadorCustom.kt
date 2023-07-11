package com.mik3.ejemplorecyclerview

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView

/**
 * Elaborado por Miguel Ángel Vázquez García
 **/
class AdaptadorCustom (items:ArrayList<Platillo>,var listener: ClickListener,var longClickListener: LongClickListener): RecyclerView.Adapter<AdaptadorCustom.ViewHolder>(){

    var items: ArrayList<Platillo> ?= null
    var multiseleccion=false

    var itemsSeleccionados: ArrayList<Int>?=null
    var viewHolder:ViewHolder?= null

    init {
        this.items=items
        this.itemsSeleccionados = ArrayList()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdaptadorCustom.ViewHolder {
        val vista =  LayoutInflater.from(parent?.context).inflate(R.layout.template_platillo,parent,false)
        viewHolder= ViewHolder(vista,listener,longClickListener)
        return viewHolder!!
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items?.get(position)
        holder.foto?.setImageResource(item?.foto!!)
        holder.nombre?.text = item?.nombre
        holder.precio?.text = "$"+item?.precio.toString()
        holder.rating?.rating = item?.rating!!

        if(itemsSeleccionados?.contains(position)!!){
            holder.vista.setBackgroundColor(Color.LTGRAY)
        }else{
            holder.vista.setBackgroundColor(Color.GREEN)
        }
    }

    fun iniciarActionMode(){
        multiseleccion=true
    }

    fun destruirActionMode(){
        multiseleccion=false
        itemsSeleccionados?.clear()
        notifyDataSetChanged()
    }

    fun terminarActionMode(){
        //eliminar elementos seleccionados
        for (item in itemsSeleccionados!!){
            itemsSeleccionados?.remove(item)
        }
        multiseleccion=false
        notifyDataSetChanged()

    }

    fun seleccionarItem(index:Int){
        if(multiseleccion){
            if(itemsSeleccionados?.contains(index)!!){
                itemsSeleccionados?.remove(index)
            }else{
                itemsSeleccionados?.add(index)
            }

            notifyDataSetChanged()
        }
    }

    fun obtenerNumeroDeElementosSeleccionados():Int{
        return itemsSeleccionados?.count()!!
    }

    fun eliminarSeleccionados(){
        if(itemsSeleccionados?.count()!!>0){
            var itemsEliminados = ArrayList<Platillo>()

            for(index in itemsSeleccionados!!){
                itemsEliminados.add(items?.get(index)!!)
            }
            items?.removeAll(itemsEliminados)
            itemsSeleccionados?.clear()
        }
    }

    override fun getItemCount(): Int {
        return items?.count()!!
    }

    class ViewHolder(vista:View,listener:ClickListener, longClickListener: LongClickListener): RecyclerView.ViewHolder(vista),View.OnClickListener,View.OnLongClickListener{
        var vista = vista
        var foto:ImageView ?= null
        var nombre: TextView ?= null
        var precio: TextView ?= null
        var rating: RatingBar ?= null
        var listener:ClickListener ?= null
        var longListener:LongClickListener ?= null

        init {
            this.foto= vista.findViewById(R.id.ivFoto)
            this.nombre = vista.findViewById(R.id.tvNombre)
            this.precio = vista.findViewById(R.id.tvPrecio)
            this.rating = vista.findViewById(R.id.tvRating)

            this.listener=listener
            this.longListener = longClickListener

            vista.setOnClickListener(this)
            vista.setOnLongClickListener(this)
        }

        override fun onClick(v: View?) {
            this.listener?.onClick(v!!,adapterPosition)
        }

        override fun onLongClick(v: View?): Boolean {
            this.longListener?.longClick(v!!,adapterPosition)
            return true
        }
    }

}