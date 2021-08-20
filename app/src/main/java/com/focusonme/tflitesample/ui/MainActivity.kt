package com.focusonme.tflitesample.ui

import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.focusonme.tflitesample.R
import com.focusonme.tflitesample.extension.glide.GlideHelper
import com.focusonme.tflitesample.extension.opencv.saveImageToFile
import com.focusonme.tflitesample.extension.rx.plusAssign
import com.focusonme.tflitesample.util.createNewFile
import com.orhanobut.logger.Logger
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import org.tensorflow.lite.DataType
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import org.tensorflow.lite.support.tensorbuffer.TensorBufferUint8


class MainActivity : AppCompatActivity(), ImageAdapter.ItemClickListener {

    companion object {

        private const val MODEL_FILE_PATH = "model.tflite"

        private const val IMAGE_HEIGHT = 512

        private const val IMAGE_WIDTH = 680

        private val images = arrayOf(
            R.drawable.case1_input,
            R.drawable.case2_input,
            R.drawable.case3_input,
            R.drawable.case4_input,
            R.drawable.case5_input,
            R.drawable.case6_input
        )

        private val inputImages = arrayOf(
            R.drawable.input_case1,
            R.drawable.input_case2,
            R.drawable.input_case3,
            R.drawable.input_case4,
            R.drawable.input_case5,
            R.drawable.input_case6
        )
    }

    private val placeHolder = ColorDrawable(Color.BLACK)

    private val processor by lazy {
        ImageProcessor.Builder()
            .add(ResizeOp(IMAGE_HEIGHT, IMAGE_WIDTH * 2, ResizeOp.ResizeMethod.BILINEAR))
            .build()
    }

    private var inpaintModel: Interpreter? = null

    private var inputTensor: TensorImage? = null

    private var outputTensor: TensorBuffer? = null

    private var currentPosition: Int = 0

    private val adapter by lazy {
        ImageAdapter().apply { setItemClickListener(this@MainActivity) }
    }

    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initInpaintModel()
        initViews()
    }

    private fun initViews() {
        showInputImage(0)
        with(rvImage) {
            layoutManager = GridLayoutManager(this@MainActivity, 3)
            adapter = this@MainActivity.adapter
        }
        tvRun.setOnClickListener {
            runInpaintModel(inputImages[currentPosition])
        }
    }

    private fun initInpaintModel() {
        inpaintModel = Interpreter(
            FileUtil.loadMappedFile(this, MODEL_FILE_PATH),
            Interpreter.Options().apply { setAllowBufferHandleOutput(true) }
        )
    }

    private fun runInpaintModel(resourceId: Int) {
        initInputTensor(resourceId)
        initOutputTensor()

        if (inputTensor == null || outputTensor == null) return

        Logger.d("run model with case${currentPosition + 1}")
        val start = System.currentTimeMillis()
        disposables += observeInpaintModel()
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { progressBar.visibility = View.VISIBLE }
            .doOnSuccess { progressBar.visibility = View.GONE }
            .doOnError { progressBar.visibility = View.GONE }
            .subscribe({ path ->
                val end = System.currentTimeMillis()
                val duration = end - start
                Logger.d("running inpaint model success: $duration ms")
                tvTime.text = "${duration / 1000}.${duration % 1000} seconds"
                GlideHelper.loadImage(this@MainActivity, path, ivResult)
            }) { Logger.d("running inpaint model fail: $it") }
    }

    private fun initInputTensor(resourceId: Int) {
        inputTensor = TensorImage(DataType.FLOAT32)
            .apply { load(getInputImageBitmap(resourceId)) }
            .also { processor.process(it) }
    }

    private fun initOutputTensor() {
        if (inpaintModel == null) return

        outputTensor = TensorBufferUint8.createFixedSize(
            inpaintModel!!.getOutputTensor(0)!!.shape(),
            inpaintModel!!.getOutputTensor(0)!!.dataType()
        )
    }

    private fun observeInpaintModel() = Single
        .fromCallable { inpaintModel!!.run(inputTensor!!.buffer, outputTensor!!.buffer) }
        .map { createNewFile().also { outputTensor!!.buffer.saveImageToFile(it) } }
        .subscribeOn(Schedulers.newThread())

    override fun onItemClick(position: Int) = showInputImage(position)

    private fun showInputImage(position: Int) {
        currentPosition = position
        GlideHelper.loadImage(this, images[position], ivInput)
        ivResult.setImageDrawable(placeHolder)
        tvTime.text = ""
    }

    private fun getInputImageBitmap(resourceId: Int) =
        BitmapFactory.decodeResource(resources, resourceId)
}