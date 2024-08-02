package com.example.a3dmodel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.a3dmodel.databinding.FragmentMainBinding
import io.github.sceneview.SceneView
import io.github.sceneview.math.Position
import io.github.sceneview.math.Scale
import io.github.sceneview.node.ModelNode
import kotlinx.coroutines.launch

class MainFragment : Fragment() {
    private lateinit var sceneView: SceneView
    private lateinit var binding: FragmentMainBinding
    private val hdrFiles = listOf(
        "environments/studio_small_09_2k.hdr",
        "environments/754-hdri-skies-com.hdr",
        "environments/806-hdri-skies-com.hdr",
        "environments/808-hdri-skies-com.hdr",
        "environments/814-hdri-skies-com.hdr"
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)

        sceneView = binding.sceneView
        binding.no754.setOnClickListener { changeEnvironment(hdrFiles[1]) }
        binding.no806.setOnClickListener { changeEnvironment(hdrFiles[2]) }
        binding.no808.setOnClickListener { changeEnvironment(hdrFiles[3]) }
        binding.no814.setOnClickListener { changeEnvironment(hdrFiles[4]) }

        viewLifecycleOwner.lifecycleScope.launch {
            changeEnvironment(hdrFiles[0])
            sceneView.cameraNode.apply {
                position = Position(z = 0.2f)
            }
//            val modelFile = "models/material_suite.glb"
            val modelFile = "models/Old_Bag.glb"
            val modelInstance = sceneView.modelLoader.createModelInstance(modelFile)
            val modelNode = ModelNode(modelInstance = modelInstance, scaleToUnits = 2.0f)
            modelNode.scale = Scale(0.05f)
            sceneView.addChildNode(modelNode)
        }

        return binding.root
    }

    private fun changeEnvironment(hdrFile: String) {
        lifecycleScope.launch {
            binding.progressCircular.isVisible = true
            sceneView.environmentLoader.loadHDREnvironment(hdrFile).apply {
                sceneView.indirectLight = this?.indirectLight
                sceneView.skybox = this?.skybox
                binding.progressCircular.isVisible = false
            }
        }
    }
}