    package com.koraspond.washershub.Fragments

    import android.os.Bundle
    import androidx.fragment.app.Fragment
    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import android.widget.ArrayAdapter
    import androidx.databinding.DataBindingUtil
    import androidx.recyclerview.widget.LinearLayoutManager
    import com.koraspond.washershub.Adapters.HomeMenuAdapter
    import com.koraspond.washershub.R
    import com.koraspond.washershub.Utils.FragmentHelper
    import com.koraspond.washershub.Utils.clickInterface
    import com.koraspond.washershub.databinding.FragmentCatDetailBinding
    import com.koraspond.washershub.databinding.FragmentHomeBinding

    class HomeFragment : Fragment(),clickInterface {
    lateinit var binding: FragmentHomeBinding
        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
            // Inflate the layout for this fragment
            return binding.root
        }


        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            binding.catRcv.layoutManager = LinearLayoutManager(requireContext())
        //    binding.catRcv.adapter = HomeMenuAdapter(requireContext(),this@HomeFragment)

            binding.filter.setOnClickListener {
                requireActivity().supportFragmentManager.
                beginTransaction().setCustomAnimations(android.R.anim.slide_in_left,android.R.anim.slide_out_right)

                    .replace(R.id.root, CategoriesFragment()).commit()
            }

            setAreaSpinner()
            setSortSpinner()
        }

        fun setSortSpinner(){

            var sort:ArrayList<String> = ArrayList<String>()
            sort.add("Sort")
            val arrayAdapter: ArrayAdapter<String> =
                ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, sort)
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.categoriesSpinner.setAdapter(arrayAdapter)
        }
        fun setAreaSpinner(){
            var areas:ArrayList<String> = ArrayList<String>()
            areas.add("Area")
            areas.add("Malir")
            areas.add("Gulshan")
            val arrayAdapter: ArrayAdapter<String> =
                ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, areas)
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.areaSpinner.setAdapter(arrayAdapter)


        }


        override fun onClick(pos: Int) {

            var fragmentManager = requireActivity().supportFragmentManager
            var carDetail = CatDetailFragment()
           // FragmentHelper.openFragment(fragmentManager, carDetail)
        }

    }