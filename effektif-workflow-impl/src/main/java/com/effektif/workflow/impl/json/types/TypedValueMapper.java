/* Copyright (c) 2014, Effektif GmbH.
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
 * limitations under the License. */
package com.effektif.workflow.impl.json.types;

import com.effektif.workflow.api.model.TypedValue;
import com.effektif.workflow.api.types.DataType;
import com.effektif.workflow.api.workflowinstance.VariableInstance;
import com.effektif.workflow.impl.data.DataTypeService;
import com.effektif.workflow.impl.json.JsonReader;
import com.effektif.workflow.impl.json.JsonTypeMapper;
import com.effektif.workflow.impl.json.JsonWriter;


/**
 * Maps a {@link String} to a JSON string field for serialisation and deserialisation.
 *
 * @author Tom Baeyens
 */
public class TypedValueMapper extends BeanMapper<TypedValue> implements JsonTypeMapper<TypedValue> {

  public static final TypedValueMapper INSTANCE = new TypedValueMapper();
  
  DataTypeService dataTypeService;

  public TypedValueMapper() {
    super(VariableInstance.class);
  }

  @Override
  public Class<TypedValue> getMappedClass() {
    return TypedValue.class;
  }

  @Override
  public void write(TypedValue typeValue, JsonWriter jsonWriter) {
    DataType dataType = typeValue.getDataType();
    Object value = typeValue.getValue();
    if (dataType==null && value!=null) {
      dataType = mappings.getTypeByValue(value);
      typeValue.setDataType(dataType);
    }
    super.write(typeValue, jsonWriter);
  }

  @Override
  public TypedValue read(Object jsonValue, JsonReader jsonReader) {
    TypedValue typedValue = super.read(jsonValue, jsonReader);
    DataType dataType = typedValue.getDataType();
    Object jsonVariableValue = typedValue.getValue();
    if (jsonVariableValue!=null && dataType!=null) {
      Object objectVariableValue = jsonReader.readObject(jsonVariableValue, dataType.getValueType());
      typedValue.setValue(objectVariableValue);
    }
    return typedValue;
  }
}