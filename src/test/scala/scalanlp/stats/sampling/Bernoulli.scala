package scalanlp.stats.sampling;

/*
 Copyright 2009 David Hall, Daniel Ramage
 
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

import org.scalatest._;
import org.scalatest.junit._;
import org.scalatest.prop._;
import org.scalacheck._;
import org.junit.runner.RunWith

import scalanlp.stats.DescriptiveStats._;

@RunWith(classOf[JUnitRunner])
class BernoulliTest extends FunSuite with Checkers {
  import Arbitrary.arbitrary;
  test("Probability Of") {
    check( Prop.forAll { (d2: Double)=> {
        val d = d2.abs % 1.0;
        val b = new Bernoulli(d);
        b.probabilityOf(true) == d &&
        b.probabilityOf(false) == (1-d)
      }
    })
  }

  val NUM_SAMPLES = 30000;
  val TOL = 1E-2;

  test("mean and variance -- sampling") {
    val d = 0.5;
    val b = new Bernoulli(d);
    val (m,v) = meanAndVariance(b.samples.take(NUM_SAMPLES).map(x => if(x) 1.0 else 0.0));
    (m - b.mean).abs < TOL && (v - b.variance).abs < TOL;
  }

}  
