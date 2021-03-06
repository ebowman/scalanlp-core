package scalanlp.collection.mutable;

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

import scala.collection.mutable.Map;
import scala.collection.generic._;

/** Represents a two dimensional array as a map. Mostly it's just a convenient wrapper.
 * A default value is mandatory so that all elements of the array are properly initialized.
 * 
 * @author dlwh
 */
abstract class Grid2[V:ClassManifest](k1Size : Int, k2Size: Int) extends Map[(Int,Int),V] with Function2[Int,Int,V] {

  /**
  * Users should override this class.
  */
  def default(k1Size : Int, k2Size: Int): V;

  override def default(key: (Int,Int)):V = default(key._1,key._2)

  private val arr = Array.tabulate(k1Size * k2Size) { idx => default(idx/k2Size, idx%k2Size) };
  
  override def apply(key: (Int,Int)):V = apply(key._1,key._2);

  def apply(i :Int, j: Int):V = arr(i * k2Size + j);
  
  def get( key: (Int,Int)) = Some(apply(key._1,key._2));
  
  def update(i:Int, j: Int, v: V) {
    arr(i*k2Size+j) = v;
  }

  override def update(key: (Int,Int), v:V) {
    update(key._1,key._2,v);
  }
  
  def -=(key : (Int,Int))  = { this(key._1,key._2) = default(key._1,key._2); this; }

  override val size = arr.size;

  def iterator = arr.iterator.zipWithIndex map { case (v,i) => ((i/k2Size,i%k2Size),v)}
  override def keys = (0 until arr.length).iterator map (i => (i/k2Size,i%k2Size));
  override def values = arr.iterator;
}
