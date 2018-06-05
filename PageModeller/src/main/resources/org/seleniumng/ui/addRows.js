/*******************************************************************************
 * Copyright 2018 Niranjan Khare
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
function add_HeaderRow(){
	var tname = 'propsview';
	var pageName = document.getElementById('pageName').value;
	var elTable = document.getElementById(tname);
	var respFields = Promise.resolve(getTableData(tname, pageName));
	Promise.resolve(respFields).then(function (tableData ){
	var headerRow = document.getElementById('headerRow');
	var dbColumns = tableData[0];
	var indexToHide = dbColumns.indexOf('CONTROLNAME');
	var selectIndex = dbColumns.indexOf('STANDARDCLASS');
	for (var i = 0; i < dbColumns.length; i++) {
		var th = document.createElement('th');
		if (i< indexToHide){
			th.setAttribute ('style', 'display:none;');
		}
        headerRow.appendChild(th).innerHTML=dbColumns[i];
    }
	headerRow.appendChild(document.createElement('th')).innerHTML='More properties';
	add_UpdateRow (tableData);
	
	});
}
function add_PagesHeaderRow (){
	var tname = 'PAGES';
	var elTable = document.getElementById(tname);
	var respFields = Promise.resolve(getTableData(tname));
	Promise.resolve(respFields).then(function (tableData ){
	var headerRow = document.getElementById('headerRow');
	var dbColumns = tableData[0];
	var indexToHide = dbColumns.indexOf('PAGENAME');
	
	for (var i = 0; i < dbColumns.length; i++) {
		var th = document.createElement('th');
		th.appendChild(document.createTextNode(dbColumns[i]));
		th.setAttribute ('value', dbColumns[i]);
		if (i< indexToHide){
			th.setAttribute ('style', 'display:none;');
		}
        headerRow.appendChild(th);
    }
	add_UpdatePagesRows (tableData);
	});
}

function add_UpdatePagesRows (pageData){
	var tname = document.getElementById('tableName').value;
	var elTable = document.getElementById(tname);
    var headerRow = document.getElementById('headerRow');
    var headers = headerRow.getElementsByTagName('th');
	var dbColumns = [];	
	for (i = 0; i < headers.length; i++){
		dbColumns.push(headers[i].textContent);
	}
	var parentSelect = getData ('/fetch/libdatabase/availablepages');
	
	Promise.resolve(parentSelect).then(function (parentData){
	var indexToHide = dbColumns.indexOf('PAGENAME');
	var selectIndex = dbColumns.indexOf('PARENTID');
	
	var rowIterator;
	if (pageData != null)
		rowIterator = pageData.length;
	else 
		rowIterator = 2; /* To force it to run once for row addition */
		for (var k = 1; k < rowIterator; k++) {
			var row = (elTable.getElementsByTagName('tbody')[0]).insertRow(-1);
				var rowCount = elTable.getElementsByTagName('tbody')[0].rows.length;
				var rowIdmain = 'Row'+ (rowCount-1);
				row.id = rowIdmain;
				for (var r=0; r<dbColumns.length; r++){
					var rowId = rowIdmain + '.' + dbColumns[r];
					var cell = row.insertCell(-1);
					var inputElement ;
					if (r !== selectIndex){
						inputElement = document.createElement('input');
					} else{
						inputElement = document.createElement('select');
						/*if (r == selectIndex){*/
						getSelectControlt(parentData, inputElement);
						/*}*/
					} 
						
					if (r< indexToHide){
						cell.setAttribute ('style', 'display:none;');
					} 
					inputElement.setAttribute ('name', rowId);
					inputElement.id = inputElement.name;
					
					cell.appendChild(inputElement);
					if (pageData != null)
						inputElement.value = pageData[k][r];
					else 
						inputElement.value = '';
				}
			  if (pageData == null)
					break;	
			}
	});
	
}
var propertyMap = null;

function add_UpdateRow(someData){
	var tname = 'propsview';
	var pageName = document.getElementById('pageName').value;
	var elTable = document.getElementById(tname);
	var headerRow = document.getElementById('headerRow');
    var headers = headerRow.getElementsByTagName('th');
	var dbColumns = [];	
	for (i = 0; i < headers.length-1; i++){
		dbColumns.push(headers[i].textContent);
	}
	var stdClasses = Promise.resolve(getData('/fetch/libdatabase/getstandardypes'));
	var locatoryTypes = Promise.resolve(getData('/fetch/getlocatorytypes'));
	Promise.all([someData, stdClasses,locatoryTypes]).then(function (allData){
	var tableData = allData[0];
	var stdClassesData = allData[1];
	var supportedLocatorTypes = allData[2];
	/*var dbColumns ;
	if (tableData != null)
		dbColumns = tableData[0];
	else
		dbColumns = allData[3];
	*/
	console.log (dbColumns);
	var indexToHide = dbColumns.indexOf('CONTROLNAME');
	var selectIndex = dbColumns.indexOf('LOCATORTYPE');
	var rowIterator;
	if (tableData != null)
		rowIterator = tableData.length;
	else 
		rowIterator = 2; /* To force it to run once for row addition */
		for (var k = 1; k < rowIterator; k++) {
			var row = (elTable.getElementsByTagName('tbody')[0]).insertRow(-1);
				var rowCount = elTable.getElementsByTagName('tbody')[0].rows.length;
				var rowIdmain = 'Row'+ (rowCount-1);
				row.id = rowIdmain;
				for (var r=0; r<dbColumns.length; r++){
					var rowId = rowIdmain + '.' + dbColumns[r];
					var cell = row.insertCell(-1);
					var inputElement ;
					if (r < selectIndex){
						inputElement = document.createElement('input');
					} else{
						inputElement = document.createElement('select');
						if (r == selectIndex){
							getSelectControlt(supportedLocatorTypes, inputElement);
						}
						if (r > selectIndex){
							getSelectControlt(stdClassesData, inputElement);
						}
					} 
						
					if (r< indexToHide){
						cell.setAttribute ('style', 'display:none;');
					} 
					inputElement.setAttribute ('name', rowId);
					inputElement.id = inputElement.name;
					
					cell.appendChild(inputElement);
					if (tableData != null)
						inputElement.value = tableData[k][r];
					
				}
				
				var popupBtn = document.createElement('button'); 
				popupBtn.type = 'button'; 
				popupBtn.setAttribute
					  ('onclick','showMoreProps(this)');
				popupBtn.id = rowIdmain +
					  '.popupBtn'; 
				popupBtn.appendChild(document.createTextNode("Define More\nProperties")); 
			  popupBtn.style.resize = 'none';
			  popupBtn.setAttribute ('rowid', rowIdmain); 
			  var cellButton = row.insertCell(-1); 
			  cellButton.appendChild(popupBtn); 
			  var cellPopupDiv = row.insertCell(-1); 
			  cellPopupDiv.setAttribute ('style', 'visibility:hidden;'); 
			  var popupDiv = document.createElement('div'); 
			  popupDiv.id = rowIdmain +'.popupDiv';
			  popupDiv.setAttribute ('style', 'visibility:hidden;display:block'); 
			  popupDiv.setAttribute ('rowid', rowIdmain); 
			  cellPopupDiv.appendChild(popupDiv); 
			  if (tableData == null)
					break;	
			}
	 
		});
	}

function getStandardtypes(e){
	Promise.resolve(getData('/fetch/libdatabase/getstandardypes'))
	.then(function(resp){
		getSelectControlt(resp, e);
	})
	.catch (function(error){
		return null;
	});
}

async function getData(u){
	let response = await fetch(u);
	return response.json();
}
function getTableFields(tname){
	r = Promise.resolve(getData('/fetch/libdatabase/gettablefields?tableName='+tname)); 
	return r;
}

function getTableData(tname,pname){
	var r;
	if (pname !== undefined)
		r = Promise.resolve(getData('/fetch/libdatabase/gettabledata?tableName='+tname+"&pageName="+ pname));
	else
		r = Promise.resolve(getData('/fetch/libdatabase/gettabledata?tableName='+tname+"&pageName="+ pname));
	return r;
}

function getExtendedPageGuiData(guiId){
	r = Promise.resolve(getData('/fetch/libdatabase/pageguiextendedprops?guiId='+guiId)); 
	return r;
}

function getSelectControlt (options, element){
	for (var key in options){
		var opt = document.createElement('option');
		opt.text = options[key][0];
		opt.value = key;
		element.appendChild(opt);
	}
}

function showMoreProps(e){
	e.disabled = true;
	var rowId = e.getAttribute('rowid');
	var popup = document.getElementById(rowId+'.popupDiv');
	if (!popup.contains(popup.querySelector('div[id=\'popupTitle\']')))
		fillPopup_new (popup);
	popup.style.visibility = 'visible';
	popup.style.display = 'inline';
	showRowById(rowId);
	disableMainForm();		
}

function showRowById (id){
	var row = document.getElementById(id);
	row.style.visibility = 'visible';
}

function disableMainForm (){
	disableMainFormButtions();
	var form = document.getElementById('formMainDiv');
	form.style.visibility = 'hidden';
}

function disableMainFormButtions(){
	document.getElementById('addRow').disabled = true;
	document.getElementById('submit').disabled = true;
}

function getPopupCloseButtonForRowId (rowId) {
	var divCloseX = document.createElement('button');
	divCloseX.type = 'button';
	divCloseX.setAttribute ('rowid', rowId);
	divCloseX.setAttribute ('style','background: none; border:none; color:red; cursor:pointer;');
	divCloseX.setAttribute ('onclick','closeMoreProps(this)');
	divCloseX.appendChild(document.createTextNode("X"));
	return divCloseX;
}

function closeMoreProps(e){
	var id = e.getAttribute('rowid');
	var idPopup = id+'.popupDiv';
	document.getElementById(id+'.popupBtn').disabled = false;
	enableMainForm();
	resetRowById(id);
	var form = document.getElementById('formMainDiv');
	form.style.visibility = 'inherit';
	var row = document.getElementById(id);
	var popup = document.getElementById(idPopup);
	row.style.visibility = 'inherit';
	popup.style.visibility = 'hidden';
	popup.style.display = 'none';
}

function enableMainForm (){
	enableMainFormButtions();
	var form = document.getElementById('formMainDiv');
	form.style.visibility = 'inherit';
}
function enableMainFormButtions(){
	document.getElementById('addRow').disabled = false;
	document.getElementById('submit').disabled = false;
}

function resetRowById (id){
	var row = document.getElementById(id);
	row.style.visibility = 'inherit';

}

function sleep(ms) {
	  return new Promise(resolve => setTimeout(resolve, ms));
}
/*
function addRowToPopupXXXXX(popup,rowContent){
	var rowId = popup.getAttribute('rowId');
	var tableId = rowId+'.exPropsTable';
	console.log(tableId);
	var pTable = document.getElementById(tableId);
	var existingRowCount = pTable.rows.length;
	var contentIndex = 0;
	var displayCell;
	var displayText;
	var cellContent;
	var valueCell ;
	for (var i in rowContent){
		var row;
		displayText = rowContent[i];
		if (contentIndex>(existingRowCount-1)){
			row = pTable.insertRow(-1);	
			displayCell = row.insertCell(-1);
			cellContent = document.createElement('textarea');
			
			
			valueCell =	row.insertCell(-1);
			
		}	
		else {
			row =pTable.rows[contentIndex];
			displayCell = row.cells[0];
			valueCell = row.cells[1];
			cellContent = valueCell.getElementsByTagName("textarea")[0];
		}
		row.id = rowId;
		displayCell.innerHTML =displayText;
		cellContent.name = rowId + '.' + i;
		cellContent.id = cellContent.name;	
		cellContent.placeholder = displayText;
		valueCell.innerHTML = cellContent.outerHTML;
		contentIndex++;
	}
	for (var d=0; d<(existingRowCount-contentIndex);d++){
		pTable.deleteRow(contentIndex);
	}
	console.log ('was: existingRowCount:' + existingRowCount);
	console.log ('contentIndex:' + contentIndex);
	console.log ('new:'+ pTable.rows.length);
	
	
}
*/
function refreshPopup (popup, e){
	var val = e.value;
	var keys = JSON.parse(propertyMap[val][1]);
	var rowId= popup.getAttribute('rowId');
	
	for (var i=0; i < 9; i++){
		var placeholder = 'EXPROP'+(i+1);
		var currentRow = document.getElementById(rowId+'.POPUP.'+ placeholder);
		
		if (keys == null || keys[placeholder] == null){
			currentRow.disabled=true;
			currentRow.style.visibility = 'hidden';
			currentRow.style.display = 'none';
		} else{
			var labelCell = document.getElementById(rowId + '.' + placeholder+'.label');
			labelCell.innerHTML = keys[placeholder];
			var valueCell = document.getElementById(rowId + '.' + placeholder);
			valueCell.placeholder = labelCell.innerHTML;
			currentRow.disabled=false;
			currentRow.style.visibility = 'inherit';
			currentRow.style.display = 'block';
		}
	}
	
}
	
function getOptionId (el, mapClass){
	var i = 0;
	for (var o of el.options){
		if (o.text === mapClass){
			i = o.index;
			break;
		}
		 
		}
	return i;
}
	
	
function fillPopup_new(p){
	var rowId = p.getAttribute('rowId');
	var title = document.createElement ('div');
	title.setAttribute('id', 'popupTitle');
	title.appendChild(document.createTextNode("Define More\nProperties"));
	title.appendChild (getPopupCloseButtonForRowId(rowId));
	p.appendChild(title);
	var content = document.createElement('div');
	content.setAttribute ('id', rowId+'.contentDiv');
	content.style.visibility='hidden';
	content.style.display='none';
	content.appendChild(document.createTextNode("Extend to class:"));
	var sel = document.createElement('select');
	p.appendChild(content);
	sel.name = rowId + '.MAPPEDCLASS';
	sel.setAttribute('rowId',rowId);
	sel.setAttribute ('onchange','refreshPopup(document.getElementById("'+p.id +'"), this)');
	content.appendChild(sel);
	
	
	var guimapId = document.getElementById(rowId+ '.GUIMAPID').value;
	
	var pageName = document.getElementById('pageName').value;
	
	Promise.resolve(getData('/fetch/libdatabase/getextendedproptypes'))
	.then(function(resp){
		if (propertyMap ===null){
			propertyMap = resp;
		}
		getSelectControlt(propertyMap, sel);
		
		addExtendedPropsTableToPopup (p);
		p.appendChild(content);
			Promise.resolve(getExtendedPageGuiData(guimapId))
			 .then(function(respData){
				var flds = respData[0];
				var vals = respData[1];
				var mapClass = vals[flds.indexOf('MAPPEDCLASS')];
				console.log (resp);
				sel.value = mapClass;
				refreshPopup (p,sel);
				var keys = JSON.parse(propertyMap[mapClass][1]);
				
				for (var key in keys){
					console.log(key);
					var valueCell = document.getElementById(rowId + '.' + key);
					valueCell.value = vals[flds.indexOf(key)];
				}
				content.style.visibility='inherit';
				content.style.display='inline';
			})
			.catch (function(error){
				
			});
	})
	.catch (function(error){
		
	});
		
	
}

function addExtendedPropsTableToPopup(popup/* , operation */){

	var rowId = popup.getAttribute('rowId');
	var content = document.getElementById(rowId+'.contentDiv');
	var tableId = rowId+'.exPropsTable';
	var exPropsTable = document.createElement('table');
	
	exPropsTable.appendChild(document.createElement('tbody'));
	exPropsTable.setAttribute("id", tableId);
	content.appendChild(exPropsTable);
	console.log(tableId);
	var pTable = document.getElementById(tableId);
	var existingRowCount = pTable.rows.length;
	var contentIndex = 0;
	var labelCell;
	var labelText;
	var cellContent;
	var valueCell ;
	
	for (var i=0; i < 9; i++){
		var row = pTable.insertRow(-1);	
		var placeholder = 'EXPROP'+(i+1);
		labelText = placeholder;
		labelCell = row.insertCell(-1);
		cellContent = document.createElement('textarea');
		valueCell =	row.insertCell(-1); 
		row.id = rowId+'.POPUP.'+ placeholder;
		labelCell.innerHTML =labelText;
		cellContent.name = rowId + '.'+ 'EXPROP'+(i+1);
		cellContent.id = cellContent.name;
		labelCell.id = cellContent.id+'.label';
		cellContent.placeholder = labelText;
		valueCell.innerHTML = cellContent.outerHTML;
	}
	
	var footer = exPropsTable.appendChild(document.createElement('tfoot'));
	var footerRow = footer.insertRow(-1);	
	var btnContainer = footerRow.insertCell(-1);
}
