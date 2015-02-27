/*
 * Copyright 2014 Effektif GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.effektif.workflow.api.workflow;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.effektif.workflow.api.types.Type;


/** 
 * A binding stores a value for an activity input parameter, such as a process variable.
 *
 * <p>A binding specifies a value in one of three possible ways:</p>
 * <ol>
 * <li>a fixed value</li>
 * <li>a reference to a workflow variable, with optionally field references</li>
 * <li>a list of bindings.</li>
 * </ol>
 *
 * @author Tom Baeyens
 */
public class Binding<T> {

  protected T value;
  protected Type type;

  /** reference to the variable that will contain the input value.  
   * This is mutually exclusive with value */
  protected String variableId;
  /** the fields that should be dereferenced in the fetched variableId.  
   * This an optional value if a variableId is specified. */
  protected List<String> fields;

  /** result of resolving the expression will be provided as the value.
   * Can be used for template strings or conversion functions. */
  protected Expression expression;
  
  /** for bindings that expect a list, a list of nested bindings can be 
   * specified.  All the results will be flatened and added to the collection.
   * This is mutually exclusive with the other fields. */
  protected List<Binding<T>> bindings;


  /** the fixed value.  
   * When serializing and deserializing, the type for this value will be automatically initialized.
   * This value is mutually exclusive with variableId and expression */
  public T getValue() {
    return this.value;
  }
  /** the fixed value.  
   * When serializing and deserializing, the type for this value will be automatically initialized.
   * This value is mutually exclusive with variableId and expression */
  public void setValue(T value) {
    this.value = value;
  }
  /** the fixed value.  
   * When serializing and deserializing, the type for this value will be automatically initialized.
   * This value is mutually exclusive with variableId and expression */
  public Binding<T> value(T value) {
    this.value = value;
    return this;
  }
  
  /** the type of {@link #getValue()} */
  public Type getType() {
    return this.type;
  }
  /** the type of {@link #getValue()} */
  public void setType(Type type) {
    this.type = type;
  }
  /** the type of {@link #getValue()} */
  public Binding<T> type(Type type) {
    this.type = type;
    return this;
  }

  public List<Binding<T>> getBindings() {
    return this.bindings;
  }
  public void setBindings(List<Binding<T>> bindings) {
    this.bindings = bindings;
  }
  public Binding<T> bindings(List<Binding<T>> bindings) {
    this.bindings = bindings;
    return this;
  }
  public Binding<T> binding(Binding<T> binding) {
    if (bindings==null) {
      bindings = new ArrayList<>();
    }
    bindings.add(binding);
    return this;
  }

  public String getVariableId() {
    return this.variableId;
  }
  public void setVariableId(String variableId) {
    this.variableId = variableId;
  }
  public Binding variableId(String variableId) {
    this.variableId = variableId;
    return this;
  }

  public List<String> getFields() {
    return this.fields;
  }
  public void setField(List<String> fields) {
    this.fields = fields;
  }
  public Binding<T> field(String field) {
    if (fields==null) {
      fields = new ArrayList<>();
    }
    fields.add(field);
    return this;
  }
  public Binding<T> fields(String[] fields) {
    if (fields!=null) {
      for (String field: fields) {
        field(field);
      }
    }
    return this;
  }


  /** .-separated notation that starts with the variableId and then 
   * specifies the fields to be dereferenced 
   * eg "myVariableId.variableField.nestedField" */
  public Binding<T> variableField(String variableFieldExpression) {
    if (variableFieldExpression==null) {
      return this;
    }
    StringTokenizer tokenizer = new StringTokenizer(variableFieldExpression, ".");
    if (!tokenizer.hasMoreTokens()) {
      return this;
    }
    this.variableId = tokenizer.nextToken();
    while (tokenizer.hasMoreTokens()) {
      if (fields == null) {
        fields = new ArrayList<>();
      }
      fields.add(tokenizer.nextToken());
    }
    return this;
  }
  
  public void setFields(List<String> fields) {
    this.fields = fields;
  }

  public Expression getExpression() {
    return this.expression;
  }
  public void setExpression(Expression expression) {
    this.expression = expression;
  }
  public Binding expression(Expression expression) {
    this.expression = expression;
    return this;
  }
  public Binding expression(String expression) {
    this.expression = new Expression()
      .script(expression);
    return this;
  }
}
