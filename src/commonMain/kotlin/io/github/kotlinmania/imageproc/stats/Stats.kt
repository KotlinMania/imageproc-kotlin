// port-lint: source stats.rs
package io.github.kotlinmania.imageproc.stats

import io.github.kotlinmania.image.Luma
import io.github.kotlinmania.image.Rgb
import io.github.kotlinmania.image.images.GenericImageView
import io.github.kotlinmania.image.images.GrayImage
import kotlin.jvm.JvmName
import kotlin.math.log10
import kotlin.math.sqrt

/**
 * A minimum and maximum value returned by [minMax].
 */
public data class MinMax<T>(
    /** The minimum value */
    public val min: T,
    /** The maximum value */
    public val max: T,
)

/**
 * Returns the minimum and maximum values per channel in a grayscale image.
 *
 * @throws IllegalArgumentException if the image is empty.
 */
@JvmName("minMaxGray")
public fun minMax(image: GenericImageView<Luma<UByte>>): List<MinMax<UByte>> {
    val (w, h) = image.dimensions()
    require(w > 0u && h > 0u) { "cannot find the min_max() of an empty image" }

    var minVal = 255u.toUByte()
    var maxVal = 0u.toUByte()

    for (y in 0u until h) {
        for (x in 0u until w) {
            val pix = image.getPixel(x, y).l
            if (pix < minVal) minVal = pix
            if (pix > maxVal) maxVal = pix
        }
    }
    return listOf(MinMax(minVal, maxVal))
}

@JvmName("min_maxGray")
public fun min_max(image: GenericImageView<Luma<UByte>>): List<MinMax<UByte>> = minMax(image)

/**
 * Returns the minimum and maximum values per channel in an RGB image.
 *
 * @throws IllegalArgumentException if the image is empty.
 */
@JvmName("minMaxRgb")
public fun minMax(image: GenericImageView<Rgb<UByte>>): List<MinMax<UByte>> {
    val (w, h) = image.dimensions()
    require(w > 0u && h > 0u) { "cannot find the min_max() of an empty image" }

    var minR = 255u.toUByte()
    var maxR = 0u.toUByte()
    var minG = 255u.toUByte()
    var maxG = 0u.toUByte()
    var minB = 255u.toUByte()
    var maxB = 0u.toUByte()

    for (y in 0u until h) {
        for (x in 0u until w) {
            val pix = image.getPixel(x, y)
            if (pix.r < minR) minR = pix.r
            if (pix.r > maxR) maxR = pix.r
            if (pix.g < minG) minG = pix.g
            if (pix.g > maxG) maxG = pix.g
            if (pix.b < minB) minB = pix.b
            if (pix.b > maxB) maxB = pix.b
        }
    }
    return listOf(MinMax(minR, maxR), MinMax(minG, maxG), MinMax(minB, maxB))
}

@JvmName("min_maxRgb")
public fun min_max(image: GenericImageView<Rgb<UByte>>): List<MinMax<UByte>> = minMax(image)

/**
 * A set of per-channel histograms from an image with 8 bits per channel.
 */
public class ChannelHistogram(
    /** Per-channel histograms. */
    public val channels: List<UIntArray>,
)

/**
 * Returns a vector of per-channel histograms for a grayscale image.
 */
@JvmName("histogramGray")
public fun histogram(image: GenericImageView<Luma<UByte>>): ChannelHistogram {
    val hist = UIntArray(256)
    val (w, h) = image.dimensions()
    for (y in 0u until h) {
        for (x in 0u until w) {
            val idx = image.getPixel(x, y).l.toInt()
            hist[idx] += 1u
        }
    }
    return ChannelHistogram(listOf(hist))
}

/**
 * Returns a vector of per-channel histograms for an RGB image.
 */
@JvmName("histogramRgb")
public fun histogram(image: GenericImageView<Rgb<UByte>>): ChannelHistogram {
    val rHist = UIntArray(256)
    val gHist = UIntArray(256)
    val bHist = UIntArray(256)
    val (w, h) = image.dimensions()
    for (y in 0u until h) {
        for (x in 0u until w) {
            val pix = image.getPixel(x, y)
            rHist[pix.r.toInt()] += 1u
            gHist[pix.g.toInt()] += 1u
            bHist[pix.b.toInt()] += 1u
        }
    }
    return ChannelHistogram(listOf(rHist, gHist, bHist))
}

/**
 * A set of per-channel cumulative histograms from an image with 8 bits per channel.
 */
public class CumulativeChannelHistogram(
    /** Per-channel cumulative histograms. */
    public val channels: List<UIntArray>,
)

/**
 * Returns per-channel cumulative histograms for a grayscale image.
 */
@JvmName("cumulativeHistogramGray")
public fun cumulativeHistogram(image: GenericImageView<Luma<UByte>>): CumulativeChannelHistogram {
    val h = histogram(image)
    val cum = h.channels[0].copyOf()
    for (i in 1 until 256) {
        cum[i] += cum[i - 1]
    }
    return CumulativeChannelHistogram(listOf(cum))
}

@JvmName("cumulative_histogramGray")
public fun cumulative_histogram(image: GenericImageView<Luma<UByte>>): CumulativeChannelHistogram =
    cumulativeHistogram(image)

/**
 * Returns per-channel cumulative histograms for an RGB image.
 */
@JvmName("cumulativeHistogramRgb")
public fun cumulativeHistogram(image: GenericImageView<Rgb<UByte>>): CumulativeChannelHistogram {
    val h = histogram(image)
    val cumChannels =
        h.channels.map { channel ->
            val cum = channel.copyOf()
            for (i in 1 until 256) {
                cum[i] += cum[i - 1]
            }
            cum
        }
    return CumulativeChannelHistogram(cumChannels)
}

@JvmName("cumulative_histogramRgb")
public fun cumulative_histogram(image: GenericImageView<Rgb<UByte>>): CumulativeChannelHistogram =
    cumulativeHistogram(image)

/**
 * Returns the [p]th percentile of the pixel intensities in an image.
 *
 * We define the `p`th percentile intensity to be the least `x` such
 * that at least `p`% of image pixels have intensity less than or equal to `x`.
 *
 * @throws IllegalArgumentException if `p > 100`.
 */
public fun percentile(image: GrayImage, p: UByte): UByte {
    require(p <= 100u.toUByte()) { "requested percentile must be <= 100" }

    val cumHist = cumulativeHistogram(image).channels[0]
    val total = cumHist[255].toULong()

    for (i in 0 until 256) {
        if (100uL * cumHist[i].toULong() / total >= p.toULong()) {
            return i.toUByte()
        }
    }
    throw IllegalStateException("unreachable")
}

@JvmName("meanSquaredErrorGray")
public fun meanSquaredError(left: GenericImageView<Luma<UByte>>, right: GenericImageView<Luma<UByte>>): Double {
    require(left.width() == right.width() && left.height() == right.height()) {
        "image dimensions must match: (${left.width()}x${left.height()}) vs (${right.width()}x${right.height()})"
    }
    var sumSquaredDiffs = 0.0
    val w = left.width()
    val h = left.height()
    for (y in 0u until h) {
        for (x in 0u until w) {
            val fc = left.getPixel(x, y).l.toDouble()
            val fd = right.getPixel(x, y).l.toDouble()
            val diff = fc - fd
            sumSquaredDiffs += diff * diff
        }
    }
    val count = (w.toULong() * h.toULong() * 1uL).toDouble()
    return sumSquaredDiffs / count
}

@JvmName("mean_squared_errorGray")
public fun mean_squared_error(left: GenericImageView<Luma<UByte>>, right: GenericImageView<Luma<UByte>>): Double =
    meanSquaredError(left, right)

@JvmName("meanSquaredErrorRgb")
public fun meanSquaredError(left: GenericImageView<Rgb<UByte>>, right: GenericImageView<Rgb<UByte>>): Double {
    require(left.width() == right.width() && left.height() == right.height()) {
        "image dimensions must match: (${left.width()}x${left.height()}) vs (${right.width()}x${right.height()})"
    }
    var sumSquaredDiffs = 0.0
    val w = left.width()
    val h = left.height()
    for (y in 0u until h) {
        for (x in 0u until w) {
            val p = left.getPixel(x, y)
            val q = right.getPixel(x, y)
            val dr = p.r.toDouble() - q.r.toDouble()
            val dg = p.g.toDouble() - q.g.toDouble()
            val db = p.b.toDouble() - q.b.toDouble()
            sumSquaredDiffs += dr * dr + dg * dg + db * db
        }
    }
    val count = (w.toULong() * h.toULong() * 3uL).toDouble()
    return sumSquaredDiffs / count
}

@JvmName("mean_squared_errorRgb")
public fun mean_squared_error(left: GenericImageView<Rgb<UByte>>, right: GenericImageView<Rgb<UByte>>): Double =
    meanSquaredError(left, right)

@JvmName("rootMeanSquaredErrorGray")
public fun rootMeanSquaredError(left: GenericImageView<Luma<UByte>>, right: GenericImageView<Luma<UByte>>): Double =
    sqrt(meanSquaredError(left, right))

@JvmName("root_mean_squared_errorGray")
public fun root_mean_squared_error(left: GenericImageView<Luma<UByte>>, right: GenericImageView<Luma<UByte>>): Double =
    rootMeanSquaredError(left, right)

@JvmName("rootMeanSquaredErrorRgb")
public fun rootMeanSquaredError(left: GenericImageView<Rgb<UByte>>, right: GenericImageView<Rgb<UByte>>): Double =
    sqrt(meanSquaredError(left, right))

@JvmName("root_mean_squared_errorRgb")
public fun root_mean_squared_error(left: GenericImageView<Rgb<UByte>>, right: GenericImageView<Rgb<UByte>>): Double =
    rootMeanSquaredError(left, right)

@JvmName("peakSignalToNoiseRatioGray")
public fun peakSignalToNoiseRatio(original: GenericImageView<Luma<UByte>>, noisy: GenericImageView<Luma<UByte>>): Double {
    val maxVal = 255.0
    val mse = meanSquaredError(original, noisy)
    return 20.0 * log10(maxVal) - 10.0 * log10(mse)
}

@JvmName("peak_signal_to_noise_ratioGray")
public fun peak_signal_to_noise_ratio(original: GenericImageView<Luma<UByte>>, noisy: GenericImageView<Luma<UByte>>): Double =
    peakSignalToNoiseRatio(original, noisy)

@JvmName("peakSignalToNoiseRatioRgb")
public fun peakSignalToNoiseRatio(original: GenericImageView<Rgb<UByte>>, noisy: GenericImageView<Rgb<UByte>>): Double {
    val maxVal = 255.0
    val mse = meanSquaredError(original, noisy)
    return 20.0 * log10(maxVal) - 10.0 * log10(mse)
}

@JvmName("peak_signal_to_noise_ratioRgb")
public fun peak_signal_to_noise_ratio(original: GenericImageView<Rgb<UByte>>, noisy: GenericImageView<Rgb<UByte>>): Double =
    peakSignalToNoiseRatio(original, noisy)
