package com.biki.quickmeal.ui.recipe

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.biki.quickmeal.R
import com.biki.quickmeal.data.repository.remote.model.Meal
import kotlinx.android.synthetic.main.fragment_instructions.*

/**
 * Fragment responsible for displaying recipe instructions of the selected recipe
 */
class RecipeInstructionsFragment : Fragment() {
    private var meal: Meal? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_instructions, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getParcelable<Meal>("meal").apply {
            setData(this!!)
            meal = this
        }
        youtubeButton.setOnClickListener {
            watchYoutubeVideo(meal?.youtubeLink)
        }

    }

    private fun watchYoutubeVideo(url: String?) {
        url?.let {
            val youtubeIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(url)
            )
            try {
                startActivity(youtubeIntent)
            } catch (ex: ActivityNotFoundException) {
                Toast.makeText(activity, R.string.cannot_open, Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun setData(meal: Meal) {
        txt_recipe?.text = meal.name
        txt_instructions?.text = meal.instructions
        txt_area?.text = String.format("%s %s", meal.area, meal.category)
    }

    companion object {
        fun newInstance(meal: Meal): Fragment {
            val fragment = RecipeInstructionsFragment()
            val bundle = Bundle()
            bundle.putParcelable("meal", meal)
            fragment.arguments = bundle
            return fragment
        }
    }
}