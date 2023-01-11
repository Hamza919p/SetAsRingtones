package com.wmt.setasringtones.fragments

import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController

open class BaseBottomTabFragment : Fragment() {
    var isNavigated = false

    fun navigateWithAction(action: NavDirections) {
        isNavigated = true
        findNavController().navigate(action)
    }

    fun navigate(resId: Int) {
        isNavigated = true
        findNavController().navigate(resId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (!isNavigated)
            requireActivity().onBackPressedDispatcher.addCallback(this) {
                val navController = findNavController()
                if (navController.currentBackStackEntry?.destination?.id != null) {
                    findNavController().popBackStackAllInstances(
                        navController.currentBackStackEntry?.destination?.id!!,
                        true
                    )
                } else
                    navController.popBackStack()
            }
    }

    private fun NavController.popBackStackAllInstances(
        destination: Int,
        inclusive: Boolean
    ): Boolean {
        var popped: Boolean
        while (true) {
            popped = popBackStack(destination, inclusive)
            if (!popped) {
                break
            }
        }
        return popped
    }
}
