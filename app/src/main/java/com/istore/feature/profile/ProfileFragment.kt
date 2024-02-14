package com.istore.feature.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.istore.R
import com.istore.common.IFragment
import com.istore.databinding.FragmentProfileBinding
import com.istore.feature.auth.AuthActivity
import com.istore.feature.favorites.FavoriteProductsActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : IFragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProfileViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            tvFavoritesList.setOnClickListener {
                startActivity(Intent(requireContext(), FavoriteProductsActivity::class.java))
            }

        }
    }

    override fun onResume() {
        super.onResume()
        checkUserAuthentication()
    }

    private fun checkUserAuthentication() {
        if (viewModel.isAuthenticated) {
            binding.tvUsername.text = viewModel.username
            binding.tvLoginOrLogout.text = getString(R.string.logout)
            binding.tvLoginOrLogout.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_logout, 0)
            binding.tvLoginOrLogout.setOnClickListener {
                viewModel.logout()
                checkUserAuthentication()
            }

        } else {
            binding.tvUsername.text = getString(R.string.guest_user)
            binding.tvLoginOrLogout.text = getString(R.string.login_title)
            binding.tvLoginOrLogout.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_login, 0)
            binding.tvLoginOrLogout.setOnClickListener {
                startActivity(Intent(requireContext(), AuthActivity::class.java))
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}