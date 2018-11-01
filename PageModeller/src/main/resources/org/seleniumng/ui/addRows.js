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

function loadTable(tname,hideBeforeColumn, pageName){
	var elTable = document.getElementById(tname);
	var respFields = Promise.resolve(getTableData(tname,pageName));
	Promise.resolve(respFields).then(function (tableData ){
	var headerRow = document.getElementById('headerRow');
	var dbColumns = tableData[0];
	var indexToHide = dbColumns.indexOf(hideBeforeColumn);
	switch (tname){
	case 'PAGES':
		headerRow.appendChild(addHeaderColumn(dbColumns[0], true)); 
		headerRow.appendChild(addHeaderColumn(dbColumns[1], false));
		headerRow.appendChild(addHeaderColumn(dbColumns[2], false));
		headerRow.appendChild(addHeaderColumn(dbColumns[3], false));
		break;
	case 'PROPSVIEW':
		headerRow.appendChild(addHeaderColumn(dbColumns[0], true)); 
		headerRow.appendChild(addHeaderColumn(dbColumns[1], true));
		headerRow.appendChild(addHeaderColumn(dbColumns[2], false));
		headerRow.appendChild(addHeaderColumn(dbColumns[3], false));
		headerRow.appendChild(addHeaderColumn(dbColumns[4], false));
		headerRow.appendChild(addHeaderColumn(dbColumns[5], false));
		headerRow.appendChild(addHeaderColumn(dbColumns[6], false));
		headerRow.appendChild(document.createElement('th')).innerHTML='More properties';
		break;
	}
	addTableRows (tableData,hideBeforeColumn); 
	addCheckboxesToHeaderRow(headerRow );
	});
}

function addHeaderColumn (hdrTxt, bHide){
	var th = document.createElement('th');
	th.setAttribute('class','header');
	th.appendChild(document.createTextNode(hdrTxt));
	if (bHide){
		th.setAttribute ('style', 'display:none;');
	}
	return th;
}

function addCheckboxesToHeaderRow (h){
	var thEnable = document.createElement('th');
	var thDelete = document.createElement('th');
	var chkBoxSelect = document.createElement('input');
	chkBoxSelect.setAttribute('type', 'checkbox');
	chkBoxSelect.setAttribute ('onclick', 'enableRow(this)');
	var chkBoxRow = chkBoxSelect.cloneNode(true); /* need this for rows */
	chkBoxSelect.setAttribute ('rowId','headerRow');
	chkBoxSelect.setAttribute ('onclick','selectAllClass(this);');
	var chkBoxDelete = chkBoxSelect.cloneNode(true);
	chkBoxSelect.setAttribute ('onclick','selectAllClass(this);document.querySelector("#headerRow .rowDelete").disabled=!this.checked;');
	chkBoxSelect.setAttribute('class', 'rowSelect');
	chkBoxDelete.setAttribute('class', 'rowDelete'); 
	chkBoxSelect.setAttribute ('id',headerRow.id+'.select');
	chkBoxDelete.setAttribute ('id',headerRow.id+'.delete');
	chkBoxDelete.disabled=true;
	thEnable.appendChild(chkBoxSelect);
	thEnable.appendChild (document.createTextNode('All'));
	
	thDelete.appendChild(chkBoxDelete);
	thDelete.appendChild (document.createTextNode('Delete'));
	headerRow.insertBefore(thDelete, headerRow.firstChild);
	headerRow.insertBefore(thEnable, headerRow.firstChild);

}
function addCheckboxesToRow (e, delKey){
	var chkBoxRow = document.createElement('input');
	chkBoxRow.setAttribute('type', 'checkbox');
	chkBoxRow.setAttribute ('onclick', 'enableRow(this)');
	var cell = e.insertCell(0);
	chkBoxRow.setAttribute ('id', e.id+'.delete');
	chkBoxRow.setAttribute ('rowId', e.id);
	chkBoxRow.setAttribute ('name', e.id+'.delete');
	chkBoxRow.setAttribute ('class', 'rowDelete');
	var chkDelete = chkBoxRow.cloneNode(true);
	var v = e.querySelector('input[name="'+e.id+'.'+delKey+'"]');
	console.log(v.value);
	chkDelete.setAttribute ('value', v.value);
	if (delKey==='PAGEID'){
		chkDelete.setAttribute('onclick','markPageForDelete(this);');
	} else{
		chkDelete.setAttribute('onclick','markForDelete(this);');

	}	
	chkDelete.disabled=true;
	cell.appendChild(chkDelete);
	
	chkBoxRow.setAttribute ('id', e.id+'.select');
	chkBoxRow.setAttribute ('name', e.id+'.select');
	chkBoxRow.setAttribute ('class', 'rowSelect');
	chkBoxRow.setAttribute('onclick','toggleSubmit(this);');
	e.insertCell(0).appendChild(chkBoxRow.cloneNode(true));
}

function selectAllClass(e){
	var allRowSelects = document.querySelectorAll('.'+ e.getAttribute('class'));
	for (var i=1;i<allRowSelects.length;i++){
		if (allRowSelects[i].checked != e.checked)
			allRowSelects[i].click();
	}
	console.log('done');	
}

function toggleSubmit(e){
	var chkValue = e.checked;
	var rowId = e.getAttribute('rowId');
	var allRowInputs = document.querySelectorAll('#'+rowId+' input,#'+rowId+' select');
	for (var i=1;i<allRowInputs.length;i++){
		allRowInputs[i].disabled = !chkValue;
	}
}

function markPageForDelete(e){
	var rowId = e.getAttribute('rowId');
	var eRow = document.querySelector('#'+rowId);
	var parents = document.querySelectorAll('.PARENTID');
	var pageName = document.querySelectorAll('#'+rowId+ ' .PAGENAME')[0].value;
	var message = 'Following rows: ';
	var markForDelete = [];
	for (var i=0;i<parents.length;i++){
		var parentName = parents[i].selectedOptions[0].text;
		if (pageName===parentName){
			var parentRowId = parents[i].getAttribute('rowId');
			markForDelete.push(parentRowId);
		}
	}
	var bConfirm = false;
	if (e.checked ){
		bConfirm = confirm ('Following Pages heirarchy will be reset!Please Confirm!'+ markForDelete.toString());
	}
	if (bConfirm){
	for (var i=0;i<markForDelete.length;i++){
		var eSelect = document.querySelector('#'+markForDelete[i]+' .rowSelect');
		if (!eSelect.checked)
			eSelect.click();
		
		
		var eParentSelect = document.querySelector('#'+markForDelete[i]+' .PARENTID');
		eParentSelect.style.border = 'thin solid red';
		eParentSelect.value = -1;
	}	
	} else {
		e.checked=false;
		}
	var allRowInputs = document.querySelectorAll('#'+rowId+' input,#'+rowId+' select');
	for (var i=2;i<allRowInputs.length;i++){
		allRowInputs[i].disabled = e.checked;
	}
}

function markForDelete(e){
	var rowId = e.getAttribute('rowId');
	var eRow = document.querySelector('#'+rowId);
	var allRowInputs = document.querySelectorAll('#'+rowId+' input,#'+rowId+' select');
	for (var i=2;i<allRowInputs.length;i++){
		allRowInputs[i].disabled = e.checked;
	}
}

function addTableRows (tableData,hideBeforeColumn){
	var tname = document.getElementById('tableName').value;
	var elTable = document.getElementById(tname);
    var headerRow = document.getElementById('headerRow');
    var headers = headerRow.getElementsByClassName('header');
	var dbColumns = [];	
	for (i = 0; i < headers.length; i++){
		dbColumns.push(headers[i].textContent);
	}
	var promises ;
	switch (tname){
	case 'PAGES':
		var pageData=Promise.resolve(getData ('/fetch/libdatabase/availablepages'));
		promises =  Promise.all([ pageData]);
		break;
	case 'PROPSVIEW':
		var stdClasses = Promise.resolve(getData('/fetch/libdatabase/getstandardypes'));
        var locatorTypes = Promise.resolve(getData('/fetch/getlocatorytypes'));
        promises =  Promise.all([ locatorTypes, stdClasses ]);
		break;
	}
	promises.then(function (allData){
		addUpdateTable (tname, elTable,dbColumns,tableData, allData);
	});
		
}
function addUpdateTable (table, elTable,dbColumns,tableData, allData){
	var rowIterator= tableData.length;
	var k = 1;
	do{
		var row = (elTable.getElementsByTagName('tbody')[0]).insertRow(-1);
		var rowCount = elTable.getElementsByTagName('tbody')[0].rows.length;
		var rowIdMain = 'Row'+ (rowCount-1);
		row.id = rowIdMain;	
		var inputElement ;
		/* var cell = row.insertCell(-1); */
		switch (table){
		case 'PAGES':
			if (tableData.length === 0)
				addPagesRow (row, dbColumns,['','','',''],allData);
			else 
				addPagesRow (row, dbColumns,tableData[k],allData);
			addCheckboxesToRow(row,'PAGEID'); 
			break;
		case 'PROPSVIEW': 
			if (tableData.length === 0)
				addPropsViewRow (row, dbColumns,['','','','','','',''],allData);
			else 
				addPropsViewRow (row, dbColumns,tableData[k],allData);
			addCheckboxesToRow(row, 'GUIMAPID');
			break;
		}
		
		k++;
	} while (k< rowIterator);
	
}
function addInputToRow (cell,rowId,column,data,bHide,selectData){
	var inputElement ;
	var cellName = rowId + '.' + column;
	if (bHide){
		cell.setAttribute ('style', 'display:none;');
	}
	if (selectData == null)
		inputElement = document.createElement('input');
	else {
		inputElement = document.createElement('select');
		getSelectControlt(selectData, inputElement,  true);
		
	}	
	inputElement.setAttribute('class',column);
	inputElement.setAttribute ('name', cellName);
	inputElement.setAttribute ('rowId', rowId);
	inputElement.id = inputElement.name;
	inputElement.value = data;
	inputElement.disabled=true;
	cell.appendChild(inputElement);
	/*cell.id = cellName;*/
	return cell;
}

var propertyMap = null;

function addPagesRow (row, columns,data,selections){
	addInputToRow (row.insertCell(-1),row.id,columns[0],data[0],true);
	addInputToRow (row.insertCell(-1),row.id,columns[1],data[1],false);
	addInputToRow (row.insertCell(-1),row.id,columns[2],data[2],false,selections[0]);
	addInputToRow (row.insertCell(-1),row.id,columns[3],data[3],false);
}
function addPropsViewRow (row, columns,data,selections){
	addInputToRow (row.insertCell(-1),row.id,columns[0],data[0],true);
	addInputToRow (row.insertCell(-1),row.id,columns[1],data[1],true);
	addInputToRow (row.insertCell(-1),row.id,columns[2],data[2],false);
	addInputToRow (row.insertCell(-1),row.id,columns[3],data[3],false);
    addInputToRow (row.insertCell(-1),row.id,columns[4],data[4],false,selections[0]);
    addInputToRow (row.insertCell(-1),row.id,columns[5],data[5],false);
	addInputToRow (row.insertCell(-1),row.id,columns[6],data[6],false,selections[1]);
	var popupBtn = document.createElement('button'); 
	popupBtn.type = 'button'; 
	popupBtn.setAttribute
		  ('onclick','showMoreProps(this)');
	popupBtn.id = row.id +
		  '.popupBtn'; 
	popupBtn.appendChild(document.createTextNode("Define More\nProperties")); 
  popupBtn.style.resize = 'none';
  popupBtn.setAttribute ('rowid', row.id); 
  var cellButton = row.insertCell(-1); 
  cellButton.appendChild(popupBtn); 
  var cellPopupDiv = row.insertCell(-1); 
  cellPopupDiv.setAttribute ('style', 'visibility:hidden;'); 
  var popupDiv = document.createElement('div'); 
  popupDiv.id = row.id +'.popupDiv';
  popupDiv.setAttribute ('style', 'visibility:hidden;display:block'); 
  popupDiv.setAttribute ('rowid', row.id); 
  cellPopupDiv.appendChild(popupDiv); 
  }
function getStandardtypes(e){
	Promise.resolve(getData('/fetch/libdatabase/getstandardypes'))
	.then(function(resp){
		getSelectControlt(resp, e, false);
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

function getSelectControlt (options, element, withBlank){
	for (var key in options){
		var opt = document.createElement('option');
		opt.text = options[key][0];
		opt.value = key;
		element.appendChild(opt);
	}
	if (withBlank == true){
		var opt = document.createElement('option');
		opt.text = '';
		opt.value = '-1';
		element.insertBefore (opt, element.firstChild);
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
 * function addRowToPopupXXXXX(popup,rowContent){ var rowId =
 * popup.getAttribute('rowId'); var tableId = rowId+'.exPropsTable';
 * console.log(tableId); var pTable = document.getElementById(tableId); var
 * existingRowCount = pTable.rows.length; var contentIndex = 0; var displayCell;
 * var displayText; var cellContent; var valueCell ; for (var i in rowContent){
 * var row; displayText = rowContent[i]; if (contentIndex>(existingRowCount-1)){
 * row = pTable.insertRow(-1); displayCell = row.insertCell(-1); cellContent =
 * document.createElement('textarea');
 * 
 * 
 * valueCell = row.insertCell(-1); } else { row =pTable.rows[contentIndex];
 * displayCell = row.cells[0]; valueCell = row.cells[1]; cellContent =
 * valueCell.getElementsByTagName("textarea")[0]; } row.id = rowId;
 * displayCell.innerHTML =displayText; cellContent.name = rowId + '.' + i;
 * cellContent.id = cellContent.name; cellContent.placeholder = displayText;
 * valueCell.innerHTML = cellContent.outerHTML; contentIndex++; } for (var d=0;
 * d<(existingRowCount-contentIndex);d++){ pTable.deleteRow(contentIndex); }
 * console.log ('was: existingRowCount:' + existingRowCount); console.log
 * ('contentIndex:' + contentIndex); console.log ('new:'+ pTable.rows.length); }
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
		getSelectControlt(propertyMap, sel, false);
		
		addExtendedPropsTableToPopup (p);
		p.appendChild(content);
			Promise.resolve(getExtendedPageGuiData(guimapId))
			 .then(function(respData){
				var flds = respData[0];
				var vals = respData[1];
				var mapClass ;
				if (guimapId === '') {
					mapClass = '(No Maping)';
				}
				else {
					mapClass= vals[flds.indexOf('MAPPEDCLASS')];	
				}
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

function removeSelectCheckboxes(){
	var chkSelects = document.querySelectorAll('.rowSelect');
	for (var i=0;i< chkSelects.length;i++){
		chkSelects[i].disabled=true;
	}
}