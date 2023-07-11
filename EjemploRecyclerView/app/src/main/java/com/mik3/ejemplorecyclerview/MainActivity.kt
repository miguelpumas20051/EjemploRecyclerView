package com.mik3.ejemplorecyclerview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.view.ActionMode
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.WithHint
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    var lista:RecyclerView ?= null
    var adaptador:AdaptadorCustom ?= null
    var layoutManager:RecyclerView.LayoutManager ?= null

    var isActionMode = false
    var actionMode:ActionMode ?= null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val platillos = ArrayList<Platillo>()
        platillos.add(Platillo("Platillo 1",250.00,5.0f, R.drawable.platillo01))
        platillos.add(Platillo("Platillo 2",250.00,5.0f, R.drawable.platillo02))
        platillos.add(Platillo("Platillo 3",250.00,5.0f, R.drawable.platillo03))
        platillos.add(Platillo("Platillo 4",250.00,5.0f, R.drawable.platillo04))
        platillos.add(Platillo("Platillo 5",250.00,5.0f, R.drawable.platillo05))
        platillos.add(Platillo("Platillo 6",250.00,5.0f, R.drawable.platillo06))
        platillos.add(Platillo("Platillo 7",250.00,5.0f, R.drawable.platillo07))


        lista = findViewById(R.id.lista)
        lista?.setHasFixedSize(true)

        layoutManager = LinearLayoutManager(this)
        lista?.layoutManager=layoutManager

        val callback = object: ActionMode.Callback{
            override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                //incializar action mode
                adaptador?.iniciarActionMode()
                actionMode=mode
                //inflar menu
                menuInflater.inflate(R.menu.menu_contextual,menu!!)
                return true
            }

            override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                mode?.title="0 Seleccionados"
                return false
            }

            override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
                when(item?.itemId){
                    R.id.iEliminar->{
                        adaptador?.eliminarSeleccionados()
                    }else->{
                    return true
                }
                }
                adaptador?.terminarActionMode()
                mode?.finish()
                isActionMode=false


                return true
            }

            override fun onDestroyActionMode(mode: ActionMode?) {
                //destruir action mode
                adaptador?.destruirActionMode()
                isActionMode=false
            }

        }

        adaptador = AdaptadorCustom(platillos,object:ClickListener{
            override fun onClick(vista: View, index: Int) {
                Toast.makeText(applicationContext,platillos.get(index).nombre,Toast.LENGTH_SHORT).show()
            }
        },
        object: LongClickListener{
            override fun longClick(vista: View, index: Int) {
                if(!isActionMode){
                    startSupportActionMode(callback)
                    isActionMode=true
                    adaptador?.seleccionarItem(index)

                }else{
                    //hacer selecciones o deselecciones
                    adaptador?.seleccionarItem(index)
                }

                actionMode?.title=adaptador?.obtenerNumeroDeElementosSeleccionados().toString()+ " seleccionados"
            }

        })
        lista?.adapter=adaptador



        val swipeToRefresh = findViewById<SwipeRefreshLayout>(R.id.swipeToRefresh)
        swipeToRefresh.setOnRefreshListener {
            for(i in 1..100000000){

            }
            swipeToRefresh.isRefreshing = false
            platillos.add(Platillo("Platillo 8",250.00,5.0f, R.drawable.platillo08))
            platillos.add(Platillo("Platillo 9",250.00,5.0f, R.drawable.platillo09))
            platillos.add(Platillo("Platillo 10",250.00,5.0f, R.drawable.platillo10))
            adaptador?.notifyDataSetChanged()
        }
    }
}