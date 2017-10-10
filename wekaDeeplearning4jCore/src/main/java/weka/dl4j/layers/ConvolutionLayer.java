/*
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/*
 *    ConvolutionLayer.java
 *    Copyright (C) 2016 University of Waikato, Hamilton, New Zealand
 *
 */
package weka.dl4j.layers;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Map;

import org.deeplearning4j.nn.conf.ConvolutionMode;
import org.deeplearning4j.nn.conf.GradientNormalization;
import org.deeplearning4j.nn.conf.Updater;
import org.deeplearning4j.nn.conf.distribution.Distribution;
import org.deeplearning4j.nn.weights.WeightInit;

import org.nd4j.linalg.activations.IActivation;
import weka.core.Option;
import weka.core.OptionHandler;
import weka.core.OptionMetadata;
import weka.dl4j.distribution.NormalDistribution;
import weka.gui.ProgrammaticProperty;
import weka.dl4j.activations.ActivationIdentity;

/**
 * A version of DeepLearning4j's ConvolutionLayer that implements WEKA option handling.
 *
 * @author Christopher Beckham
 * @author Eibe Frank
 *
 * @version $Revision: 11711 $
 */
public class ConvolutionLayer extends org.deeplearning4j.nn.conf.layers.ConvolutionLayer implements OptionHandler, Serializable {

	/** The ID used to serialize this class. */
	private static final long serialVersionUID = 6905344091980568487L;

	/**
	 * Global info.
	 *
	 * @return string describing this class.
	 */
	public String globalInfo() {
		return "A convolution layer from DeepLearning4J.";
	}

	/**
	 * Constructor for setting some defaults.
	 */
	public ConvolutionLayer() {
		setLayerName("Convolution layer");
		setActivationFunction(new ActivationIdentity());
		setWeightInit(WeightInit.XAVIER);
		setDist(new NormalDistribution());
		setBiasInit(1.0);
		setConvolutionMode(ConvolutionMode.Truncate);
		setKernelSize(new int[] {5, 5});
		setStride(new int[] {1, 1});
		setPadding(new int[] {0, 0});
		this.cudnnAlgoMode = ConvolutionLayer.AlgoMode.PREFER_FASTEST;
	}

	@OptionMetadata(
					displayName = "layer name",
					description = "The name of the layer (default = Convolutional Layer).",
					commandLineParamName = "name", commandLineParamSynopsis = "-name <string>",
					displayOrder = 0)
	public String getLayerName() {
		return this.layerName;
	}
	public void setLayerName(String layerName) {
		this.layerName = layerName;
	}

	@OptionMetadata(
			displayName = "number of filters",
			description = "The number of filters.",
			commandLineParamName = "nFilters", commandLineParamSynopsis = "-nFilters <int>",
			displayOrder = 1)
	public int getNOut() { return super.getNOut(); }
	public void setNOut(int nOut) {
		this.nOut = nOut;
	}

	@OptionMetadata(
			displayName = "convolution mode",
			description = "The convolution mode (default = Truncate).",
			commandLineParamName = "mode", commandLineParamSynopsis = "-mode <string>",
			displayOrder = 2)
	public ConvolutionMode getConvolutionMode() {
		return this.convolutionMode;
	}
	public void setConvolutionMode(ConvolutionMode convolutionMode) {
		this.convolutionMode = convolutionMode;
	}

	@OptionMetadata(
			displayName = "CudnnAlgoMode",
			description = "The Cudnn algo mode (default = PREFER_FASTEST).",
			commandLineParamName = "cudnnAlgoMode", commandLineParamSynopsis = "-cudnnAlgoMode <string>",
			displayOrder = 3)
	public ConvolutionLayer.AlgoMode getCudnnAlgoMode() {
		return this.cudnnAlgoMode;
	}
	public void setCudnnAlgoMode(ConvolutionLayer.AlgoMode cudnnAlgoMode) {
		this.cudnnAlgoMode = cudnnAlgoMode;
	}

	@OptionMetadata(
			displayName = "number of columns in kernel",
			description = "The number of columns in the kernel (default = 5).",
			commandLineParamName = "kernelSizeX", commandLineParamSynopsis = "-kernelSizeX <int>",
			displayOrder = 4)
	public int getKernelSizeX() {
		return this.kernelSize[0];
	}
	public void setKernelSizeX(int kernelSize) {
		this.kernelSize[0] = kernelSize;
	}


	@OptionMetadata(
			displayName = "number of rows in kernel",
			description = "The number of rows in the kernel (default = 5).",
			commandLineParamName = "kernelSizeY", commandLineParamSynopsis = "-kernelSizeY <int>",
			displayOrder = 5)
	public int getKernelSizeY() {
		return this.kernelSize[1];
	}
	public void setKernelSizeY(int kernelSize) {
		this.kernelSize[1] = kernelSize;
	}

	@ProgrammaticProperty
	public int[] getKernelSize() {
		return this.kernelSize;
	}
	public void setKernelSize(int[] kernelSize) {
		this.kernelSize = kernelSize;
	}

	@OptionMetadata(
			displayName = "number of columns in stride",
			description = "The number of columns in the stride (default = 1).",
			commandLineParamName = "strideX", commandLineParamSynopsis = "-strideX <int>",
			displayOrder = 6)
	public int getStrideX() {
		return this.stride[0];
	}
	public void setStrideX(int stride) {
		this.stride[0] = stride;
	}

	@OptionMetadata(
			displayName = "number of rows in stride",
			description = "The number of rows in the stride (default = 1).",
			commandLineParamName = "strideY", commandLineParamSynopsis = "-strideY <int>",
			displayOrder = 7)
	public int getStrideY() {
		return this.stride[1];
	}
	public void setStrideY(int stride) {
		this.stride[1] = stride;
	}

	@ProgrammaticProperty
	public int[] getStride() {
		return this.stride;
	}
	public void setStride(int[] stride) {
		this.stride = stride;
	}

	@OptionMetadata(
			displayName = "number of columns in padding",
			description = "The number of columns in the padding (default = 0).",
			commandLineParamName = "paddingX", commandLineParamSynopsis = "-paddingX <int>",
			displayOrder = 8)
	public int getPaddingX() {
		return this.padding[0];
	}
	public void setPaddingX(int padding) {
		this.padding[0] = padding;
	}

	@OptionMetadata(
			displayName = "number of rows in padding",
			description = "The number of rows in the padding (default = 0).",
			commandLineParamName = "paddingY", commandLineParamSynopsis = "-paddingY <int>",
			displayOrder = 9)
	public int getPaddingY() {
		return this.padding[1];
	}
	public void setPaddingY(int padding) {
		this.padding[1] = padding;
	}

	@OptionMetadata(
			displayName = "activation function",
			description = "The activation function to use (default = Identity).",
			commandLineParamName = "activation", commandLineParamSynopsis = "-activation <specification>",
			displayOrder = 10)
	public IActivation getActivationFunction() { return this.activationFn; }
	public void setActivationFunction(IActivation activationFn) {
		this.activationFn = activationFn;
	}

	@ProgrammaticProperty
	public IActivation getActivationFn() { return super.getActivationFn(); }
	public void setActivationFn(IActivation fn) {
		super.setActivationFn(fn);
	}

	@OptionMetadata(
					displayName = "weight initialization method",
					description = "The method for weight initialization (default = XAVIER).",
					commandLineParamName = "weightInit", commandLineParamSynopsis = "-weightInit <specification>",
					displayOrder = 11)
	public WeightInit getWeightInit() {
		return this.weightInit;
	}
	public void setWeightInit(WeightInit weightInit) {
		this.weightInit = weightInit;
	}

	@OptionMetadata(
					displayName = "bias initialization",
					description = "The bias initialization (default = 1.0).",
					commandLineParamName = "biasInit", commandLineParamSynopsis = "-biasInit <double>",
					displayOrder = 12)
	public double getBiasInit() {
		return this.biasInit;
	}
	public void setBiasInit(double biasInit) {
		this.biasInit = biasInit;
	}

	@OptionMetadata(
					displayName = "distribution",
					description = "The distribution (default = NormalDistribution(1e-3, 1)).",
					commandLineParamName = "dist", commandLineParamSynopsis = "-dist <specification>",
					displayOrder = 13)
	public Distribution getDist() {
		return this.dist;
	}
	public void setDist(Distribution dist) {
		this.dist = dist;
	}
	

	@OptionMetadata(
					displayName = "L1",
					description = "The L1 parameter (default = 0).",
					commandLineParamName = "L1", commandLineParamSynopsis = "-L1 <double>",
					displayOrder = 19)
	public double getL1() {
		return this.l1;
	}
	public void setL1(double l1) {
		this.l1 = l1;
	}

	@OptionMetadata(
					displayName = "L2",
					description = "The L2 parameter (default = 0).",
					commandLineParamName = "L2", commandLineParamSynopsis = "-L2 <double>",
					displayOrder = 20)
	public double getL2() {
		return this.l2;
	}
	public void setL2(double l2) {
		this.l2 = l2;
	}

	@OptionMetadata(
					displayName = "L1 bias",
					description = "The L1 bias parameter (default = 0).",
					commandLineParamName = "l1Bias", commandLineParamSynopsis = "-l1Bias <double>",
					displayOrder = 21)
	public double getBiasL1() {
		return this.l1Bias;
	}
	public void setBiasL1(double biasL1) {
		this.l1Bias = biasL1;
	}

	@OptionMetadata(
					displayName = "L2 bias",
					description = "The L2 bias parameter (default = 0).",
					commandLineParamName = "l2Bias", commandLineParamSynopsis = "-l2Bias <double>",
					displayOrder = 22)
	public double getBiasL2() {
		return this.l2Bias;
	}
	public void setBiasL2(double biasL2) {
		this.l2Bias = biasL2;
	}

	@OptionMetadata(
					displayName = "dropout parameter",
					description = "The dropout parameter (default = 0).",
					commandLineParamName = "dropout", commandLineParamSynopsis = "-dropout <double>",
					displayOrder = 23)
	public double getDropOut() {
		return this.dropOut;
	}
	public void setDropOut(double dropOut) {
		this.dropOut = dropOut;
	}

	@OptionMetadata(
					displayName = "gradient normalization method",
					description = "The gradient normalization method (default = None).",
					commandLineParamName = "gradientNormalization", commandLineParamSynopsis = "-gradientNormalization <specification>",
					displayOrder = 30)
	public GradientNormalization getGradientNormalization() {
		return this.gradientNormalization;
	}
	public void setGradientNormalization(GradientNormalization gradientNormalization) {
		this.gradientNormalization = gradientNormalization;
	}

	@OptionMetadata(
					displayName = "gradient normalization threshold",
					description = "The gradient normalization threshold (default = 1).",
					commandLineParamName = "gradNormThreshold", commandLineParamSynopsis = "-gradNormThreshold <double>",
					displayOrder = 31)
	public double getGradientNormalizationThreshold() {
		return this.gradientNormalizationThreshold;
	}
	public void setGradientNormalizationThreshold(double gradientNormalizationThreshold) {
		this.gradientNormalizationThreshold = gradientNormalizationThreshold;
	}

	// The number of channels is set automatically based on the image reader
	@ProgrammaticProperty
	public int getNIn() { return super.getNIn(); }
	public void setNIn(int nIn) {
		this.nIn = nIn;
	}

	@ProgrammaticProperty
	public int[] getPadding() {
		return this.padding;
	}
	public void setPadding(int[] padding) {
		this.padding = padding;
	}

	@ProgrammaticProperty
	public double getL1Bias() { return super.getL1Bias(); }
	public void setL1Bias(int l1bias) { super.setL1Bias(l1bias); }

	@ProgrammaticProperty
	public double getL2Bias() { return super.getL2Bias(); }
	public void setL2Bias(int l2bias) { super.setL2Bias(l2bias); }

	/**
	 * Returns an enumeration describing the available options.
	 *
	 * @return an enumeration of all the available options.
	 */
	@Override
	public Enumeration<Option> listOptions() {

		return Option.listOptionsForClass(this.getClass()).elements();
	}

	/**
	 * Gets the current settings of the Classifier.
	 *
	 * @return an array of strings suitable for passing to setOptions
	 */
	@Override
	public String[] getOptions() {

		return Option.getOptions(this, this.getClass());
	}

	/**
	 * Parses a given list of options.
	 *
	 * @param options the list of options as an array of strings
	 * @exception Exception if an option is not supported
	 */
	public void setOptions(String[] options) throws Exception {

		Option.setOptions(options, this, this.getClass());
	}
}
