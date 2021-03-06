package scalanlp.stats.sampling

import scalanlp.math.Numerics.lgamma;
import Math._;

/**
* A binomial distribution returns how many coin flips out of n are heads,
* where p is the probability of any one coin being heads.
*/
class Binomial(n: Int, p: Double)(implicit rand: RandBasis=Rand) extends DiscreteDistr[Int] with Moments[Double] {
  require(n > 0);
  require(p >= 0.0);
  def probabilityOf(k: Int) = exp(logProbabilityOf(k));
  
  override def logProbabilityOf(k: Int) = {
    require(n >= k);
    require(k >= 0);
    lgamma(n+1) - lgamma(k+1) - lgamma(n-k+1) + k * log(p) + (n-k) * log(1-p)
  }
  
  override def draw() = {
    (1 to n).map(_ => if(rand.uniform.get < p) 1 else 0).foldLeft(0)(_+_);
  }

  def mean = n * p;
  def variance = mean * (1 - p);

}
