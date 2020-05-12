package com.kevin.movieapp

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintSet
import androidx.transition.TransitionManager
import kotlinx.android.synthetic.main.activity_animate.*

class AnimateActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animate)
        addAnimationOperations()
    }

    private fun addAnimationOperations() {
        var set = false
        val constraint1 = ConstraintSet()
        constraint1.clone(constraint_root)
        val constraint2 = ConstraintSet()
        constraint2.clone(this, R.layout.keyframe_two)

        findViewById<Button>(R.id.button2).setOnClickListener{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                TransitionManager.beginDelayedTransition(constraint_root)
                val constraint = if(set) constraint1 else constraint2
                constraint.applyTo(constraint_root)
                set = !set
            }
        }

    }
}
