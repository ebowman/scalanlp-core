package scalanlp.util;

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

import scala.collection.mutable.ArrayBuffer;


/**
* Useful implicit conversions and other utilities.
* 
* @author dlwh 
*/
object Implicits extends Asserts {
  
  //
  // Extra convenience methods on Scala builtins
  //
  
  class ListExtra[T](list:List[T]) {
    def tails = new Seq[List[T]] {
      def length = list.length;
      def apply(i : Int) = list.drop(i);
      def iterator = new Iterator[List[T]] {
        private var n = list;
        def next = {
          val ret = n;
          n = ret.tail;
          ret
        }

        def hasNext = (n!=Nil)
      }
    }
  }

  implicit def listExtras[T](list : List[T]) = new ListExtra(list);

  class StringExtras(s : String) {
    /**
    * Implements Levenshtein Distance. 
    */
    def editDistance(s2 : String) = {
      if(s.length == 0) s2.length;
      else if(s2.length ==0) s.length;
      else {
        val d: Array[Array[Int]] = Array.ofDim(s.length+1,s2.length+1);
        for(i <- 0 to s.length)
          d(i)(0) = i;
        for(i <- 0 to s2.length)
          d(0)(i) = i;
        for(i <- 1 to s.length;
          j <- 1 to s2.length) {
            val cost = if(s(i-1) == s2(j-1)) 0 else 1;
            d(i)(j) = Math.min( d(i-1)(j)+1, Math.min(d(i)(j-1)+1,d(i-1)(j-1) + cost));
        }
        d(s.length)(s2.length);
      }
    }
  }
  implicit def stringExtras(s : String) = new StringExtras(s);


  implicit def anyExtras[T](a:T) = new AnyExtras[T](a);

  class AnyExtras[T](x: T) {
    /**
    * Unfold creates a list starting from a seed value. It's meant to be the 
    * opposite of List.foldr in that if there is an "inverse" of f,
    * that has some stopping criterion, then we have something like
    * list reduceRight ( f) unfoldr inversef == list
    */
    def unfoldr[R](f: T=>Option[(R,T)]): Seq[R] = {
      var result = new ArrayBuffer[R];
      var current = f(x);
      while( current != None ) {
        val next = current.asInstanceOf[Some[(R,T)]].get._2;
        val r = current.asInstanceOf[Some[(R,T)]].get._1;
        result += r;
        current = f(next);
      }
      result.reverse;
    }
  }

  implicit def tExtras[T<:AnyRef](t : T) = new AnyRefExtras[T](t);
  
  /**
   * Provides extensions to Any.
   */
  class AnyRefExtras[T<:AnyRef](t: T) {
    /**
     * if t is non-null, return it, otherwise, evaluate and return u.
     */ 
    def ?:[U>:T](u: =>U) = if(t eq null) u else t;

    /**
     * Intern for arbitrary types
     */ 
    def intern : T= {
      val in : Interner[T] = Interner.forClass(t.getClass.asInstanceOf[Class[T]])
      in(t);
    }

    /**
    * if t is non-null return Some(t), otherwise None
    */
    def toOption = if(t eq null) None else Some(t);
    
  }
  
  implicit def doubleExtras(d: Double) = new {
    def =~=(e: Double) = d ==e ||  Math.abs(d - e)/d < 1E-4;
  }

  implicit def SeqExtras[T](s: Seq[T]) = new {
    // useful subset selection
    def apply(x: Seq[Int]) = x.view.map(s);  
  }
}
