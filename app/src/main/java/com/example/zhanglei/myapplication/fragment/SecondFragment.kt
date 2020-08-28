package com.example.zhanglei.myapplication.fragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.zhanglei.myapplication.R
import kotlinx.android.synthetic.main.fragment_second.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SecondFragment : BaseFragment() {
	private var param1: String? = null
	private var param2: String? = null

	companion object {
		@JvmStatic
		fun newInstance(param1: String, param2: String) =
				SecondFragment().apply {
					arguments = Bundle().apply {
						putString(ARG_PARAM1, param1)
						putString(ARG_PARAM2, param2)
					}
				}
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		arguments?.let {
			param1 = it.getString(ARG_PARAM1)
			param2 = it.getString(ARG_PARAM2)
		}
	}


	override fun layoutResId(): Int {
		return R.layout.fragment_second
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		val data = listOf(
                "https://img.ui.cn/data/file/6/5/4/3268456.jpg?imageMogr2/auto-orient/format/jpg/strip/thumbnail/!900%3E/quality/90/",
                "https://img.ui.cn/data/file/7/5/4/3268457.jpg?imageMogr2/auto-orient/format/jpg/strip/thumbnail/!900%3E/quality/90/",
                "https://img.ui.cn/data/file/8/5/4/3268458.jpg?imageMogr2/auto-orient/format/jpg/strip/thumbnail/!900%3E/quality/90/",
                "https://img.ui.cn/data/file/9/5/4/3268459.jpg?imageMogr2/auto-orient/format/jpg/strip/thumbnail/!900%3E/quality/90/",
                "https://img.ui.cn/data/file/0/6/4/3268460.jpg?imageMogr2/auto-orient/format/jpg/strip/thumbnail/!900%3E/quality/90/",
                "https://img.ui.cn/data/file/2/6/4/3268462.jpg?imageMogr2/auto-orient/format/jpg/strip/thumbnail/!900%3E/quality/90/",
                "https://img.ui.cn/data/file/3/6/4/3268463.jpg?imageMogr2/auto-orient/format/jpg/strip/thumbnail/!900%3E/quality/90/",
                "https://img.ui.cn/data/file/4/6/4/3268464.jpg?imageMogr2/auto-orient/format/jpg/strip/thumbnail/!900%3E/quality/90/",
                "https://img.ui.cn/data/file/5/6/4/3268465.jpg?imageMogr2/auto-orient/format/jpg/strip/thumbnail/!900%3E/quality/90/",
                "https://img.ui.cn/data/file/6/6/4/3268466.jpg?imageMogr2/auto-orient/format/jpg/strip/thumbnail/!900%3E/quality/90/",
                "https://img.ui.cn/data/file/7/6/4/3268467.jpg?imageMogr2/auto-orient/format/jpg/strip/thumbnail/!900%3E/quality/90/",
                "https://img.ui.cn/data/file/8/6/4/3268468.jpg?imageMogr2/auto-orient/format/jpg/strip/thumbnail/!900%3E/quality/90/",
                "https://img.ui.cn/data/file/9/6/4/3268469.jpg?imageMogr2/auto-orient/format/jpg/strip/thumbnail/!900%3E/quality/90/",
                "https://img.ui.cn/data/file/0/7/4/3268470.jpg?imageMogr2/auto-orient/format/jpg/strip/thumbnail/!900%3E/quality/90/",
                "https://img.ui.cn/data/file/1/7/4/3268471.jpg?imageMogr2/auto-orient/format/jpg/strip/thumbnail/!900%3E/quality/90/",
                "https://img.ui.cn/data/file/3/7/4/3268473.jpg?imageMogr2/auto-orient/format/jpg/strip/thumbnail/!900%3E/quality/90/",
                "https://img.ui.cn/data/file/4/7/4/3268474.jpg?imageMogr2/auto-orient/format/jpg/strip/thumbnail/!900%3E/quality/90/",
                "https://img.ui.cn/data/file/6/7/4/3268476.jpg?imageMogr2/auto-orient/format/jpg/strip/thumbnail/!900%3E/quality/90/",
                "https://img.ui.cn/data/file/7/7/4/3268477.jpg?imageMogr2/auto-orient/format/jpg/strip/thumbnail/!900%3E/quality/90/",
                "https://img.ui.cn/data/file/8/7/4/3268478.jpg?imageMogr2/auto-orient/format/jpg/strip/thumbnail/!900%3E/quality/90/",
                "https://img.ui.cn/data/file/9/7/4/3268479.jpg?imageMogr2/auto-orient/format/jpg/strip/thumbnail/!900%3E/quality/90/",
                "https://img.ui.cn/data/file/1/8/4/3268481.jpg?imageMogr2/auto-orient/format/jpg/strip/thumbnail/!900%3E/quality/90/",
                "https://img.ui.cn/data/file/2/8/4/3268482.jpg?imageMogr2/auto-orient/format/jpg/strip/thumbnail/!900%3E/quality/90/",
                "https://img.ui.cn/data/file/3/8/4/3268483.jpg?imageMogr2/auto-orient/format/jpg/strip/thumbnail/!900%3E/quality/90/",
                "https://img.ui.cn/data/file/4/8/4/3268484.jpg?imageMogr2/auto-orient/format/jpg/strip/thumbnail/!900%3E/quality/90/",
                "https://img.ui.cn/data/file/6/8/4/3268486.jpg?imageMogr2/auto-orient/format/jpg/strip/thumbnail/!900%3E/quality/90/",
                "https://img.ui.cn/data/file/7/8/4/3268487.jpg?imageMogr2/auto-orient/format/jpg/strip/thumbnail/!900%3E/quality/90/",
                "https://img.ui.cn/data/file/8/8/4/3268488.jpg?imageMogr2/auto-orient/format/jpg/strip/thumbnail/!900%3E/quality/90/",
                "https://img.ui.cn/data/file/0/9/4/3268490.jpg?imageMogr2/auto-orient/format/jpg/strip/thumbnail/!900%3E/quality/90/",
                "https://img.ui.cn/data/file/1/9/4/3268491.jpg?imageMogr2/auto-orient/format/jpg/strip/thumbnail/!900%3E/quality/90/",
                "https://img.ui.cn/data/file/2/9/4/3268492.jpg?imageMogr2/auto-orient/format/jpg/strip/thumbnail/!900%3E/quality/90/",
                "https://img.ui.cn/data/file/3/9/4/3268493.jpg?imageMogr2/auto-orient/format/jpg/strip/thumbnail/!900%3E/quality/90/",
                "https://img.ui.cn/data/file/4/9/4/3268494.jpg?imageMogr2/auto-orient/format/jpg/strip/thumbnail/!900%3E/quality/90/",
                "https://img.ui.cn/data/file/6/9/4/3268496.jpg?imageMogr2/auto-orient/format/jpg/strip/thumbnail/!900%3E/quality/90/",
                "https://img.ui.cn/data/file/7/9/4/3268497.jpg?imageMogr2/auto-orient/format/jpg/strip/thumbnail/!900%3E/quality/90/",
                "https://img.ui.cn/data/file/8/9/4/3268498.jpg?imageMogr2/auto-orient/format/jpg/strip/thumbnail/!900%3E/quality/90/",
                "https://img.ui.cn/data/file/9/9/4/3268499.jpg?imageMogr2/auto-orient/format/jpg/strip/thumbnail/!900%3E/quality/90/",
                "https://img.ui.cn/data/file/0/0/5/3268500.jpg?imageMogr2/auto-orient/format/jpg/strip/thumbnail/!900%3E/quality/90/",
                "https://img.ui.cn/data/file/1/0/5/3268501.jpg?imageMogr2/auto-orient/format/jpg/strip/thumbnail/!900%3E/quality/90/",
                "https://img.ui.cn/data/file/3/0/5/3268503.jpg?imageMogr2/auto-orient/format/jpg/strip/thumbnail/!900%3E/quality/90/",
                "https://img.ui.cn/data/file/4/0/5/3268504.jpg?imageMogr2/auto-orient/format/jpg/strip/thumbnail/!900%3E/quality/90/",
                "https://img.ui.cn/data/file/5/0/5/3268505.jpg?imageMogr2/auto-orient/format/jpg/strip/thumbnail/!900%3E/quality/90/",
                "https://img.ui.cn/data/file/6/0/5/3268506.jpg?imageMogr2/auto-orient/format/jpg/strip/thumbnail/!900%3E/quality/90/",
                "https://img.ui.cn/data/file/8/0/5/3268508.jpg?imageMogr2/auto-orient/format/jpg/strip/thumbnail/!900%3E/quality/90/",
                "https://img.ui.cn/data/file/9/0/5/3268509.jpg?imageMogr2/auto-orient/format/jpg/strip/thumbnail/!900%3E/quality/90/",
                "https://img.ui.cn/data/file/0/1/5/3268510.jpg?imageMogr2/auto-orient/format/jpg/strip/thumbnail/!900%3E/quality/90/",
                "https://img.ui.cn/data/file/1/1/5/3268511.jpg?imageMogr2/auto-orient/format/jpg/strip/thumbnail/!900%3E/quality/90/",
                "https://img.ui.cn/data/file/3/1/5/3268513.jpg?imageMogr2/auto-orient/format/jpg/strip/thumbnail/!900%3E/quality/90/",
                "https://img.ui.cn/data/file/4/1/5/3268514.jpg?imageMogr2/auto-orient/format/jpg/strip/thumbnail/!900%3E/quality/90/",
                "https://img.ui.cn/data/file/5/1/5/3268515.jpg?imageMogr2/auto-orient/format/jpg/strip/thumbnail/!900%3E/quality/90/",
                "https://img.ui.cn/data/file/6/1/5/3268516.jpg?imageMogr2/auto-orient/format/jpg/strip/thumbnail/!900%3E/quality/90/",
                "https://img.ui.cn/data/file/7/1/5/3268517.jpg?imageMogr2/auto-orient/format/jpg/strip/thumbnail/!900%3E/quality/90/",
                "https://img.ui.cn/data/file/8/1/5/3268518.jpg?imageMogr2/auto-orient/format/jpg/strip/thumbnail/!900%3E/quality/90/",
                "https://img.ui.cn/data/file/9/1/5/3268519.jpg?imageMogr2/auto-orient/format/jpg/strip/thumbnail/!900%3E/quality/90/",
                "https://img.ui.cn/data/file/1/2/5/3268521.jpg?imageMogr2/auto-orient/format/jpg/strip/thumbnail/!900%3E/quality/90/",
                "https://img.ui.cn/data/file/2/2/5/3268522.jpg?imageMogr2/auto-orient/format/jpg/strip/thumbnail/!900%3E/quality/90/",
                "https://img.ui.cn/data/file/3/2/5/3268523.jpg?imageMogr2/auto-orient/format/jpg/strip/thumbnail/!900%3E/quality/90/",
                "https://img.ui.cn/data/file/4/2/5/3268524.jpg?imageMogr2/auto-orient/format/jpg/strip/thumbnail/!900%3E/quality/90/",
                "https://img.ui.cn/data/file/5/2/5/3268525.jpg?imageMogr2/auto-orient/format/jpg/strip/thumbnail/!900%3E/quality/90/",
                "https://img.ui.cn/data/file/6/2/5/3268526.jpg?imageMogr2/auto-orient/format/jpg/strip/thumbnail/!900%3E/quality/90/",
                "https://img.ui.cn/data/file/7/2/5/3268527.jpg?imageMogr2/auto-orient/format/jpg/strip/thumbnail/!900%3E/quality/90/",
                "https://img.ui.cn/data/file/8/2/5/3268528.jpg?imageMogr2/auto-orient/format/jpg/strip/thumbnail/!900%3E/quality/90/"
        )
		recyclerView.adapter = Adapter(data)
		recyclerView.layoutManager = LinearLayoutManager(requireContext())
	}

	class Adapter(private val data: List<String>) : RecyclerView.Adapter<Adapter.ViewHolder>() {


		class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
			val imageView: ImageView = itemView.findViewById(R.id.imageView)
		}

		override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
			val itemView = LayoutInflater.from(parent.context)
					.inflate(R.layout.item_recycler_view, parent, false)
			return ViewHolder(itemView)
		}

		override fun onBindViewHolder(holder: ViewHolder, position: Int) {
			Glide.with(holder.imageView.context)
					.load(data[position])
					.placeholder(ColorDrawable(Color.CYAN))
					.skipMemoryCache(true)
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
					.into(holder.imageView)
		}

		override fun getItemCount(): Int {
			return data.size
		}

	}
}