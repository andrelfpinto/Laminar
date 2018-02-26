package com.raquo.laminar

import com.raquo.dombuilder.generic.KeyImplicits
import com.raquo.dombuilder.generic.builders.SetterBuilders
import com.raquo.dombuilder.generic.syntax.SyntaxImplicits
import com.raquo.dombuilder.jsdom.JsCallback
import com.raquo.domtypes.generic.Modifier
import com.raquo.domtypes.generic.keys.{Attr, EventProp, Prop, Style}
import com.raquo.laminar.emitter.EventPropOps
import com.raquo.laminar.experimental.airstream.core.Observable
import com.raquo.laminar.nodes.{ReactiveElement, ReactiveHtmlElement, ReactiveNode, ReactiveText}
import com.raquo.laminar.receivers.{AttrReceiver, PropReceiver, StyleReceiver}
import org.scalajs.dom

import scala.scalajs.js.|

trait Implicits
  extends SyntaxImplicits[ReactiveNode, dom.Element, dom.Node, dom.Event, JsCallback]
  with KeyImplicits[ReactiveNode, dom.Element, dom.Node]
  with SetterBuilders[ReactiveNode, dom.Element, dom.Node]
  with DomApi
{

  @inline implicit def toAttrReceiver[V](attr: Attr[V]): AttrReceiver[V] = {
    new AttrReceiver(attr)
  }

  @inline implicit def toPropReceiver[V, DomV](prop: Prop[V, DomV]): PropReceiver[V, DomV] = {
    new PropReceiver(prop)
  }

  @inline implicit def toStyleReceiver[V](style: Style[V]): StyleReceiver[V] = {
    new StyleReceiver(style)
  }

  @inline implicit def toEventPropOps[Ev <: dom.Event](eventProp: EventProp[Ev]): EventPropOps[Ev] = {
    new EventPropOps(eventProp)
  }

  @inline implicit def textToNode(text: String): ReactiveText = {
    new ReactiveText(text)
  }

  @inline implicit def reactiveElementToReactiveHtmlElement(element: ReactiveElement[dom.html.Element]): ReactiveHtmlElement = {
    new ReactiveHtmlElement(element)
  }

  implicit def metaModifierToFlatModifier[El](makeModifier: El => Modifier[El]): Modifier[El] = {
    new Modifier[El] {
      override def apply(element: El): Unit = makeModifier(element) apply element
    }
  }

  // @TODO[IDE] This implicit conversion is actually never used by the compiler. However, this makes the Scala plugin for IntelliJ 2017.3 happy.
  @inline implicit def intellijStringObservableAsStringOrStringObservable(stringStream: Observable[String]): Observable[String | String] = {
    stringStream.asInstanceOf[Observable[String | String]]
  }
}