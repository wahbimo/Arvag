package com.example.arvag.fragments

import CarouselAdapter
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.Button
import android.widget.MediaController
import android.widget.Toast
import android.widget.VideoView
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2

import com.example.arvag.R
import java.util.*


/**
 * A fragment that displays buttons for accessing more information and contacting.
 */

class AccueilFragment : Fragment() {
    lateinit var moreInfoButton: Button
    lateinit var contactButton: Button

    private lateinit var viewPager: ViewPager2

    private val carouselImages = listOf(
        R.drawable.bv,
        R.drawable.boat2,
        R.drawable.boat3
    )

    private var currentPage = 0
    private lateinit var timer: Timer

    var simpleVideoView: VideoView? = null

    var mediaControls: MediaController? = null

    /**
     * Inflates the layout for the fragment.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container          The parent view that the fragment's UI should be attached to.
     * @param savedInstanceState A Bundle containing any saved instance state information.
     * @return The View for the fragment's UI.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_accueil, container, false)
        return view
    }

    /**
     * Called when the fragment's view has been created.
     *
     * @param view               The inflated View object representing the fragment's UI.
     * @param savedInstanceState A Bundle containing any saved instance state information.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        moreInfoButton = view.findViewById(R.id.buttonsite)


        moreInfoButton.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.arvag.org/"))
            startActivity(browserIntent)
        }
        contactButton = view.findViewById(R.id.buttoncontact)
        contactButton.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.arvag.org/contact/"))
            startActivity(browserIntent)
        }

        viewPager = view.findViewById(R.id.carouselViewPager)
        val carouselAdapter = CarouselAdapter(carouselImages)
        viewPager.adapter = carouselAdapter

        // Start automatic carousel rotation
        startCarouselRotation()

        simpleVideoView = view.findViewById<View>(R.id.videoView) as VideoView

        if (mediaControls == null) {
            // creating an object of media controller class
            mediaControls = MediaController(requireContext())

            // set the anchor view for the video view
            mediaControls!!.setAnchorView(this.simpleVideoView)
        }

        // set the media controller for video view
        simpleVideoView!!.setMediaController(mediaControls)

        // set the absolute path of the video file which is going to be played
        simpleVideoView!!.setVideoURI(Uri.parse("android.resource://"
                + requireContext().packageName + "/" + R.raw.video))

        simpleVideoView!!.requestFocus()

        // starting the video
        simpleVideoView!!.start()

        // display a toast message
        // after the video is completed
        simpleVideoView!!.setOnCompletionListener {
            Toast.makeText(requireContext(), "Video completed",
                Toast.LENGTH_LONG).show()
            true
        }

        // display a toast message if any
        // error occurs while playing the video
        simpleVideoView!!.setOnErrorListener { mp, what, extra ->
            Toast.makeText(requireContext(), "An Error Occurred " +
                    "While Playing Video !!!", Toast.LENGTH_LONG).show()
            false
        }



    }

    private fun startCarouselRotation() {
        timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                currentPage++
                if (currentPage >= carouselImages.size) {
                    currentPage = 0
                }
                activity?.runOnUiThread {
                    viewPager.setCurrentItem(currentPage, true)
                }
            }
        }, 3000, 3000) // Change images every 3 seconds
    }

    override fun onDestroyView() {
        super.onDestroyView()
        timer.cancel() // Cancel the timer to stop carousel rotation
    }

}