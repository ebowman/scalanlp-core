package scalanlp.data.process;

import scalanlp.data._;
import scalanlp.counters._
import Counters._

/**
* Filter that removes rare word that occur in fewer than threshold documents
* Syntax: new RemoveRareWords(10) apply (data);
*
* @author dlwh
*/
class RemoveRareWords(threshold:Int) {
  def apply[T,Obs<:Observation[Seq[T]]](data: Seq[Obs]) = {
    val c = DoubleCounter[T]();
    for(d <- data;
        w <- Set() ++ d.features) {
        c.incrementCount(w,1);
    }

    for(d <- data) 
      yield for(seq <- d)
      yield for(w <- seq if c(w) >= threshold) yield w;
  }
}
