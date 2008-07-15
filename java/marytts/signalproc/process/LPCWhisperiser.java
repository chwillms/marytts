/**
 * Copyright 2004-2006 DFKI GmbH.
 * All Rights Reserved.  Use is subject to license terms.
 * 
 * Permission is hereby granted, free of charge, to use and distribute
 * this software and its documentation without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of this work, and to
 * permit persons to whom this work is furnished to do so, subject to
 * the following conditions:
 * 
 * 1. The code must retain the above copyright notice, this list of
 *    conditions and the following disclaimer.
 * 2. Any modifications must be clearly marked as such.
 * 3. Original authors' names are not deleted.
 * 4. The authors' names are not used to endorse or promote products
 *    derived from this software without specific prior written
 *    permission.
 *
 * DFKI GMBH AND THE CONTRIBUTORS TO THIS WORK DISCLAIM ALL WARRANTIES WITH
 * REGARD TO THIS SOFTWARE, INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS, IN NO EVENT SHALL DFKI GMBH NOR THE
 * CONTRIBUTORS BE LIABLE FOR ANY SPECIAL, INDIRECT OR CONSEQUENTIAL
 * DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR
 * PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS
 * ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF
 * THIS SOFTWARE.
 */

package marytts.signalproc.process;

import java.io.File;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import marytts.signalproc.analysis.LpcAnalyser.LpCoeffs;
import marytts.signalproc.window.Window;
import marytts.util.data.BufferedDoubleDataSource;
import marytts.util.data.audio.AudioDoubleDataSource;
import marytts.util.data.audio.DDSAudioInputStream;
import marytts.util.math.MathUtils;


/**
 * @author Marc Schr&ouml;der
 *
 */
public class LPCWhisperiser extends LPCAnalysisResynthesis
{
    protected double whisperAmount; //Amount of whispered voice at the output between 0.5 (half whispered+half unmodified) and 1.0 (full whispered)
    protected double oneMinusWhisperAmount; //1.0-whisperAmount
    
    public LPCWhisperiser(int predictionOrder, double amount)
    {
        super(predictionOrder);
        this.whisperAmount = amount;
        this.whisperAmount = Math.max(0.0, amount);
        this.whisperAmount = Math.min(1.0, amount);
        this.oneMinusWhisperAmount = 1.0-this.whisperAmount;
    }
    
    public LPCWhisperiser(int predictionOrder)
    {
        super(predictionOrder);
        whisperAmount = 1.0;
    }
    
    /**
     * Replace residual with white noise
     */
    protected void processLPC(LpCoeffs coeffs, double[] residual)
    {
        // Determine average residual energy:
        double totalResidualEnergy = coeffs.getGain() * coeffs.getGain();
        double avgAbsAmplitude = Math.sqrt(totalResidualEnergy / residual.length);
        double maxAbsAmplitude = 2*avgAbsAmplitude;
        double spread = 2*maxAbsAmplitude;
        for (int i=0; i<residual.length; i++)
            residual[i] = whisperAmount*spread*(Math.random()-0.5) + oneMinusWhisperAmount* residual[i];
    }

    public static void main(String[] args) throws Exception
    {
        for (int i=0; i<args.length; i++) {
            AudioInputStream inputAudio = AudioSystem.getAudioInputStream(new File(args[i]));
            int samplingRate = (int)inputAudio.getFormat().getSampleRate();
            AudioDoubleDataSource signal = new AudioDoubleDataSource(inputAudio);
            int frameLength = Integer.getInteger("signalproc.lpcanalysissynthesis.framelength", 512).intValue();
            int predictionOrder = Integer.getInteger("signalproc.lpcwhisperiser.predictionorder", 20).intValue();
            FrameOverlapAddSource foas = new FrameOverlapAddSource(signal, Window.HANNING, true, frameLength, samplingRate,
                    new LPCWhisperiser(predictionOrder));
            DDSAudioInputStream outputAudio = new DDSAudioInputStream(new BufferedDoubleDataSource(foas), inputAudio.getFormat());
            String outFileName = args[i].substring(0, args[i].length()-4) + "_lpcwhisperised.wav";
            AudioSystem.write(outputAudio, AudioFileFormat.Type.WAVE, new File(outFileName));
        }

    }

}
