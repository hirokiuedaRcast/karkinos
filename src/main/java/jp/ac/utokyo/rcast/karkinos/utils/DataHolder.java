/*
Copyright Hiroki Ueda

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package jp.ac.utokyo.rcast.karkinos.utils;

import java.util.ArrayList;
import java.util.List;

import jp.ac.utokyo.rcast.karkinos.exec.CapInterval;

public class DataHolder {
	public int getNormalcnt() {
		return normalcnt;
	}

	public int getTumorcnt() {
		return tumorcnt;
	}

	int normalcnt = 0;
	int tumorcnt = 0;
	List<CapInterval> list = new ArrayList<CapInterval>();
	int len = 0;

	public void put(int _normalcnt, int _tumorcnt, CapInterval civ) {
		normalcnt = normalcnt + _normalcnt;
		tumorcnt = tumorcnt + _tumorcnt;
		len = len + civ.getLength();

		list.add(civ);
	}

	long normaltotal = 0;
	long tumortotal = 0;

	public void setTotal(long normaltotal, long tumortotal) {
		this.normaltotal = normaltotal;
		this.tumortotal = tumortotal;
	}

	public float normalAdjust() {
		// rpkm like
		float f = (float) (((double)normalcnt / (double)len) * 1000 * (double)(1000000 / (double)normaltotal));
		return f;
	}

	public float tumorAdjust() {
		float f = (float) (((double)tumorcnt / (double)len) * 1000 * (double)(1000000 / (double)tumortotal));
		return f;
	}

	public double[] averages(){
		double cnvave =0;
		double cnvaveadj =0;
		double cnvhmm = 0;
		double aaf =0;
		double baf = 0;

		for(CapInterval cap:list){
			aaf = aaf + cap.getAafreq();
			baf = baf + cap.getBafreq();

			cnvave = cnvave + cap.getOriginalValue();
			cnvaveadj = cnvaveadj + cap.getDenioseValue();
			cnvhmm = cnvhmm + cap.getHMMValue();
		}

		int size = list.size();

		if(size>0){
		  aaf = aaf/size;
		  baf = baf/size;
		  cnvave = cnvave/size;
		  cnvaveadj = cnvaveadj/size;
		  cnvhmm = cnvhmm/size;
		}

		return new double[]{cnvave,cnvaveadj ,cnvhmm ,aaf,baf};
	}

	public float tumorRatio() {
		double nr =  ((double)normalcnt / (double)normaltotal);
		double tr =  ((double)tumorcnt / (double)tumortotal);

		if(nr ==0) return 1.0f;

		return (float)(tr/nr);
	}

	public float logr(){
		float val = tumorRatio();
		if(val==0)return 0;

		double d = Math.log10(val) / Math.log10(2.0);
		return (float)d;
	}
}
