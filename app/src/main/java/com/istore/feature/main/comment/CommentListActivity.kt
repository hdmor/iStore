package com.istore.feature.main.comment

import android.os.Bundle
import android.view.View
import com.istore.common.EXTRA_KEY_ID
import com.istore.common.IActivity
import com.istore.data.Comment
import com.istore.databinding.ActivityCommentListBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class CommentListActivity : IActivity() {

    private lateinit var binding: ActivityCommentListBinding
    private val viewModel: CommentListViewModel by viewModel {
        parametersOf(
            intent.extras!!.getInt(
                EXTRA_KEY_ID
            )
        )
    }
    private val adapter = CommentsListAdapter(true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommentListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            itbCommentList.onBackClickListener = View.OnClickListener { finish() }
            rvCommentsList.adapter = adapter
        }

        viewModel.progressBarLiveData.observe(this) {
            setProgressIndicator(it)
        }
        viewModel.commentsLiveData.observe(this) {
            adapter.commentsList = it as ArrayList<Comment>
        }
    }
}