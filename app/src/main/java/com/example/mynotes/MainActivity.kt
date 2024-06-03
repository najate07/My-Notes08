package com.example.mynotes

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.RadioGroup
import android.widget.Switch
import android.widget.TextView
import com.example.mynotes.ui.theme.Chord
import java.util.Random

class MainActivity : AppCompatActivity() {

    private lateinit var chordTextView: TextView
    private lateinit var startButton: Button
    private lateinit var resetButton: Button
    private lateinit var delayRadioGroup: RadioGroup
    private lateinit var minorSwitch: Switch
    private lateinit var sharpFlatSwitch: Switch

    private val handler = Handler()
    private var runnable: Runnable? = null
    private var delay = 5000 // Default to 5 seconds
    private var includeMinor = false
    private var includeSharpFlat = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        chordTextView = findViewById(R.id.chordTextView)
        startButton = findViewById(R.id.startButton)
        resetButton = findViewById(R.id.resetButton)
        delayRadioGroup = findViewById(R.id.delayRadioGroup)
        minorSwitch = findViewById(R.id.minorSwitch)
        sharpFlatSwitch = findViewById(R.id.sharpFlatSwitch)

        delayRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            delay = when (checkedId) {
                R.id.delay5 -> 5000
                R.id.delay10 -> 10000
                R.id.delay15 -> 15000
                else -> 5000
            }
        }

        minorSwitch.setOnCheckedChangeListener { _, isChecked ->
            includeMinor = isChecked
        }

        sharpFlatSwitch.setOnCheckedChangeListener { _, isChecked ->
            includeSharpFlat = isChecked
        }

        startButton.setOnClickListener {
            startChordSequence()
        }

        resetButton.setOnClickListener {
            resetChordSequence()
        }
    }

    private fun startChordSequence() {
        startButton.isEnabled = false
        resetButton.isEnabled = false

        runnable = object : Runnable {
            override fun run() {
                val chord = generateRandomChord()
                chordTextView.text = chord.value + if (chord.isMinor) "m" else "" + if (chord.isSharp) "#" else "" + if (chord.isFlat) "b" else ""
                handler.postDelayed(this, delay.toLong())
            }
        }

        handler.post(runnable!!)
    }

    private fun resetChordSequence() {
        handler.removeCallbacks(runnable!!)
        startButton.isEnabled = true
        resetButton.isEnabled = false
        chordTextView.text = "Accord"
    }

    private fun generateRandomChord(): Chord {
        val random = Random()
        val chords = Chord.values()
        var chord: Chord
        do {
            chord = chords[random.nextInt(chords.size)]
        } while ((!includeMinor && chord.isMinor) || (!includeSharpFlat && (chord.isFlat || chord.isSharp)))
        return chord
    }
}

