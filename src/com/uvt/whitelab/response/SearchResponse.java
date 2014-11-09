/**
 * Copyright (c) 2013, 2014 Tilburg University.
 * All rights reserved.
 *
 * @author MvdCamp
 */
package com.uvt.whitelab.response;

import java.util.ArrayList;
import java.util.LinkedList;

import com.uvt.whitelab.BaseResponse;
import com.uvt.whitelab.util.FieldDescriptor;
import com.uvt.whitelab.util.FieldDescriptor.ValuePair;

public class SearchResponse extends BaseResponse {

	@Override
	protected void completeRequest() {
		loadProperties();
		loadMetaDataComponents();
		loadAdvancedComponents();
		loadCQLInfoBox();
		
		String tab = this.getParameter("tab", "simple");
		this.getContext().put("maintab", "search");
		this.getContext().put("showMetaOptions", "yes");
		this.getContext().put("tab", tab);
		this.displayHtmlTemplate(this.servlet.getTemplate("search"));

	}
	
	private void loadAdvancedComponents() {
		String or = "<div class=\"row token-box\">"+
				"<div class=\"token-minus large-1 medium-1 small-1 columns\">"+
				"<a onclick=\"Whitelab.search.advanced.removeColumnOr(this)\" class=\"remove-token hide\"><img src=\"../web/img/minus.png\"></a>"+
			"</div>"+
			"<div class=\"token-input large-15 medium-15 small-15 columns\">"+
				"<div class=\"select-row\">"+
					"<div class=\"large-8 medium-8 small-8 columns\">"+
						"<select class=\"token-type\" onchange=\"Whitelab.search.advanced.setTokenInput(this);\">";
		
		for (FieldDescriptor field : this.servlet.getSearchFields()) {
			String fieldName = field.name;
			if (this.labels.containsKey(fieldName))
				fieldName = this.labels.getString(fieldName);
				String selected = "";
				if (field.name.equals("word"))
					selected = "selected";
			or = or + "<option value=\""+field.name+"\" "+selected+">"+fieldName+"</option>";
		}
		
		or = or + "</select>"+
					"</div>"+
					"<div class=\"large-8 medium-8 small-8 columns\">"+
						"<select class=\"token-operator\" onchange=\"Whitelab.search.advanced.setTokenInput(this);\">"+
							"<option value=\"is\" selected=\"\">"+this.labels.getString("advanced.is")+"</option>"+
							"<option value=\"not\">"+this.labels.getString("advanced.not")+"</option>"+
							"<option value=\"contains\">"+this.labels.getString("advanced.contains")+"</option>"+
							"<option value=\"starts\">"+this.labels.getString("advanced.starts")+"</option>"+
							"<option value=\"ends\">"+this.labels.getString("advanced.ends")+"</option>"+
							"<option value=\"regex\">"+this.labels.getString("advanced.regex")+"</option>"+
						"</select>"+
					"</div>"+
				"</div>"+
				"<div class=\"batchrow\">"+
					"<div class=\"token-batch-input large-12 medium-12 small-12 columns\">"+
						"<textarea class=\"batchlist\"></textarea>"+
						"<div class=\"collapse textchecks\"></div>"+
					"</div>"+
					"<div class=\"large-4 medium-4 small-4 columns\">"+
						"<button class=\"erase small-erase\" alt=\""+this.labels.getString("advanced.erase")+"\" onclick=\"event.preventDefault(); Whitelab.search.advanced.eraseBatchList(this);\"></button>"+
					"</div>"+
				"</div>"+
				"<div class=\"inputrow active\">"+
					"<div class=\"token-input-field large-12 medium-12 small-12 columns\">"+
						"<input placeholder=\"&lt;any&gt;\" type=\"text\">"+
					"</div>"+
					"<div class=\"loadbutton large-4 medium-4 small-4 columns\">"+
						"<div class=\"batchWordListBtn\">"+
							"<button class=\"load-small\"></button>"+
						"</div>"+
						"<input class=\"small-loadlist\" type=\"file\" onchange=\"Whitelab.search.advanced.loadBatchList(this);\">"+
					"</div>"+
				"</div>"+
				"<div class=\"token-case\">"+
					"<input type=\"checkbox\"><p>"+this.labels.getString("case")+"</p>"+
				"</div>"+
			"</div>"+
		"</div>";
		
		String and = "<div class=\"row and-box first\">"+
				"<div class=\"row or-header hide\">"+
				this.labels.getString("advanced.boxheader")+
			"</div>"+
			"<hr>"+
			or+
			"<hr>"+
			"<div class=\"row and-footer\">"+
				"<a onclick=\"Whitelab.search.advanced.addColumnOr(this)\" class=\"add-or\">OR</a>"+
			"</div>"+
		"</div>";
		
		String column = "<div class=\"construct-column large-3 medium-3 small-8 columns\">"+
			"<div class=\"row column-header\">"+
				"<div onclick=\"Whitelab.search.advanced.removeColumn(this)\" class=\"close hide\"></div>"+
			"</div>"+
			"<div class=\"row column-content\">"+
				and+
			"</div>"+
			"<div class=\"row column-footer\">"+
				"<div class=\"small-2 columns\" align=\"left\">"+
					"<a onclick=\"Whitelab.search.advanced.addColumnAnd(this)\" class=\"add-and\"><img src=\"../web/img/plus.png\"></a>"+
				"</div>"+
				"<div class=\"small-14 columns\" align=\"right\">"+
					"<div class=\"repeat switch\">"+
						"<span>"+this.labels.getString("advanced.repeat")+"</span><input type=\"number\" class=\"from\" min=\"0\" value=\"1\" /><span>"+this.labels.getString("advanced.to")+"</span><input type=\"number\" class=\"to\" min=\"1\" value=\"1\" /><span>"+this.labels.getString("advanced.times")+"</span>"+
					"</div>"+
					"<div class=\"settings\">"+
						"<a><img src=\"../web/img/settings.png\" /></a>"+
						"<ul class=\"options\">"+
							"<li><a class=\"repeat\">"+this.labels.getString("advanced.repeat")+"</a></li>"+
							"<li><a class=\"startsen\">"+this.labels.getString("advanced.sentencestart")+"</a></li>"+
							"<li><a class=\"endsen\">"+this.labels.getString("advanced.sentenceend")+"</a></li>"+
						"</ul>"+
					"</div>"+
				"</div>"+
				"<div class=\"small-16 columns\" align=\"right\">"+
					"<span class=\"advsmallbtn switch startsen\">"+this.labels.getString("advanced.sentencestart")+" <a class\"startsen\">X</a></span>"+
					"<span class=\"advsmallbtn switch endsen\">"+this.labels.getString("advanced.sentenceend")+" <a class\"endsen\">X</a></span>"+
				"</div>"+
			"</div>"+
		"</div>";

		this.getContext().put("or", or);
		this.getContext().put("and", and);
		this.getContext().put("column", column);
	}

	private void loadCQLInfoBox() {
		String box = "<div id=\"cql_info\">"
				+ "<label>"+this.labels.getString("cql.info.header")+"</label>"
				+ "<ul class=\"examples\">";
		
		int i = 1;
		while (this.labels.containsKey("cql.info.query."+i+".text")) {
			box = box+"<li><label>"+this.labels.getString("cql.info.query."+i+".text")+"</label>";
			int j = 1;
			while (this.labels.containsKey("cql.info.query."+i+".code."+j)) {
				box = box+"<span class=\"cql\">"+this.labels.getString("cql.info.query."+i+".code."+j)+"</span>";
				j++;
			}
			box = box+"</li>";
			i++;
		}
		
		box = box+"</ul></div>";
		this.getContext().put("cqlinfo", box);
	}

	private void loadProperties() {
		LinkedList<FieldDescriptor> fields = this.servlet.getSearchFields();
		LinkedList<FieldDescriptor> props = new LinkedList<FieldDescriptor>();
		
		for (FieldDescriptor field : fields) {
			if (field.getSearchField().equals("pos")) {
				FieldDescriptor newField = field;
				newField.setValidValues(new ArrayList<ValuePair>());
				for (int i = 1; i <= 12; i++) {
					newField.addValidValue(labels.getString("pos."+i+".value"),labels.getString("pos."+i+".name"));
				}
				props.add(newField);
			} else {
				props.add(field);
			}
		}
		
		this.getContext().put("properties", props);
	}

	@Override
	protected void logRequest() {
		this.servlet.log("SearchResponse");
	}

	@Override
	public SearchResponse duplicate() {
		return new SearchResponse();
	}

}
