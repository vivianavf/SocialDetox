package com.example.socialdetox

import android.content.pm.PackageManager
import android.graphics.PixelFormat
import android.os.Build
import android.os.Build.VERSION_CODES.O
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.os.Handler
import android.view.Gravity
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.Button
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.*
import android.Manifest
import kotlin.concurrent.timerTask

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [VisualDistractor.newInstance] factory method to
 * create an instance of this fragment.
 */
class VisualDistractor : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    //nuevo
    private lateinit var windowManager: WindowManager
    private var flyCount = 0;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_visual_distractor, container, false)
        var showFlyButton = view.findViewById<Button>(R.id.showFlyButton)

        windowManager = requireActivity().getSystemService(WindowManager::class.java)

        showFlyButton.setOnClickListener {
            showFly()
        }

        // Configurar temporizador para la multiplicación de moscas cada cierto intervalo de tiempo
        Timer().scheduleAtFixedRate(object: TimerTask(){
            override fun run() {
//                windowManager.runOnUiThread{
//                    multiplyFlies()
//                }
            }
        }, 5000, 5000)
        return view
    }

    private fun requestSystemAlertWindowPermission() {
        // Solicitar el permiso en tiempo de ejecución
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.SYSTEM_ALERT_WINDOW),
            REQUEST_SYSTEM_ALERT_WINDOW_PERMISSION
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_SYSTEM_ALERT_WINDOW_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // El usuario concedió el permiso, ahora puedes mostrar la ventana superpuesta
                    showFly()
                } else {
                    // El usuario denegó el permiso, toma medidas apropiadas
                    // (por ejemplo, mostrar un mensaje informando al usuario sobre la necesidad del permiso)
                }
            }
        }
    }

    private fun showFly(){
        val flyView = ImageView(requireContext())
        flyView.setImageResource(R.drawable.fly)

        val LAYOUT_FLAG : Int ;

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE
        else
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;

        var params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            LAYOUT_FLAG,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )

        params.gravity = Gravity.TOP or Gravity.START
        params.x = Random().nextInt(windowManager.defaultDisplay.width)
        params.y = Random().nextInt(windowManager.defaultDisplay.height)

        windowManager.addView(flyView, params)
//        startFlyAnimation(flyView)
    }

    private fun multiplyFlies() {
        for(i in O until 5){
            showFly()
        }
//        flyCount++

    }

    private fun startFlyAnimation(fly: View) {
        val animation = TranslateAnimation(0f, getRandomX(), 0f, getRandomY())
        animation.duration = 3000
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                windowManager.removeView(fly)
            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })
        fly.startAnimation(animation)
    }

    private fun getRandomX(): Float {
        return (0..(windowManager.defaultDisplay.width)).random().toFloat()
    }

    private fun getRandomY(): Float {
        return (0..(windowManager.defaultDisplay.height)).random().toFloat()
    }


    companion object {

        private const val REQUEST_SYSTEM_ALERT_WINDOW_PERMISSION = 1

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment VisualDistractor.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            VisualDistractor().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}