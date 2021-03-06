package scalanlp.counters;

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

import org.scalacheck._
import org.scalatest._;
import org.scalatest.junit._;
import org.scalatest.prop._;
import scalanlp.counters._;
import Counters._;
import org.junit.runner.RunWith

@RunWith(classOf[JUnitRunner])
class PairedIntCounterTest extends FunSuite with Checkers {
  import Arbitrary._;
  implicit val arbitraryCounter = Arbitrary{
    for(x <- Gen.listOf(arbitrary[(Int,Int,Int)])) yield {
      val c = PairedIntCounter[Int,Int]();
      c ++= x;
      c
    }
  };
/*
  test("copy is a deep copy") {
    check(Prop.forAll { (c:PairedIntCounter[Int,Int]) =>
      val oldSize = c.size;
      val oldTotal = c.total;
      val oldElems = Map() ++ c.iterator;
      val c2 = c.copy;
      c2.clear;
      c.size == oldSize && c.total == oldTotal;
    });
  }
  */

/*
  test("Adding to a counter makes it contain it") {
    check(Prop.forAll { (c: PairedIntCounter[Int,Int], a: Int, b:Int) =>
      val c2 = c.copy;
      c2(a).incrementCount(b,1);
      c2.contains((a,b));
    });
  }

  test("clear") {
    check(Prop.forAll{(c : PairedIntCounter[Int,Int])  => c.clear(); c.size == 0 && c.total == 0});
    check(Prop.forAll {(c: PairedIntCounter[Int,Int], i:Int)  => 
      c.clear();
      c.get(i) == None && c(i) == 0 && !c.contains(i);
    });
  }
  test("sum and clear") {
    check(Prop.forAll{(c:PairedIntCounter[Int,Int],c2: PairedIntCounter[Int,Int]) =>
      c += c2; 
      c.clear(); 
      c.size == 0 && c.total == 0
    })
  }
  */
  test("sum preserves total") {
    check( Prop.forAll { (c:PairedIntCounter[Int,Int], c2: PairedIntCounter[Int,Int])  => 
      val expTotal = c.total + c2.total;
      val maxSize = c.size + c2.size; 
      c+=c2;
      expTotal == c.total && c.size <= maxSize
    })
  }
/*
  test("scale preserves total") {
    check ( Prop.forAll { (c2:PairedIntCounter[Int,Int], i: Int)  => 
      val c = c2.copy;

      (i == 0) ||  {
        val expTotal = c.total/i;
        c /=  i
        // truncation can cause weirdnesses.
        c.total.abs <= expTotal.abs;
      }
    })
    check ( Prop.forAll { (c2:PairedIntCounter[Int,Int],i:Int)  => 
      val c = c2.copy;
      (i == 0) || {
        val expTotal = c.total*i;
        c *=  i
        c.total == expTotal;
      }
    })
  }
  */
}
