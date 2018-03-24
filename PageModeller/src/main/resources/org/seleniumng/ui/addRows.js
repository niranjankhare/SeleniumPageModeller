function add_NewRow(){
add_Row(false);
}

var propertyMap = null;

function add_Row(f){
	var table = document.getElementById('propsview');
	var rowCount = table.getElementsByTagName('tbody')[0].rows.length;
	var row =  (table.getElementsByTagName('tbody')[0]).insertRow(-1);
	var rowId = 'Row' + rowCount;
	row.id = rowId;
	row.setAttribute ('style', 'visibility:inherit;');
	var oper = document.getElementById('oper');
	
	var resp = Promise.resolve(getTableFields('propsview'));
	resp.then(function (dbColumns){
		var index = dbColumns.indexOf('CONTROLNAME');
		
		for (var i = 0; i < dbColumns.length; i++) {
			var cellContent = null;
			if (i === dbColumns.indexOf('STANDARDCLASS')) {
				cellContent = document.createElement('select');
				getStandardtypes(cellContent);
			}
			else {
				
				if (i< index){
					cellContent = document.createElement('input');
					if (!f){
						cellContent.setAttribute ('type', 'hidden');
						cellContent.disabled=true;
					}
				} else
					cellContent = document.createElement('textarea');
			} 
			cellContent.placeholder = dbColumns[i];
			cellContent.name = rowId + '.' + dbColumns[i];
			cellContent.id = cellContent.name;
			cellContent.style.resize = 'none';
			var cell = row.insertCell(-1);
			cell.appendChild(cellContent);
		}
		var popupBtn = document.createElement('button');
		popupBtn.type = 'button';
		popupBtn.setAttribute ('onclick','showMoreProps(this, "'+ oper.value +'")');
		popupBtn.id = rowId + '.popupBtn';
		popupBtn.appendChild(document.createTextNode("Define More\nProperties"));
		popupBtn.style.resize = 'none';
		popupBtn.setAttribute ('rowid', rowId);
		var cellButton = row.insertCell(-1);
		cellButton.appendChild(popupBtn);
		var cellPopupDiv = row.insertCell(-1);
		cellPopupDiv.setAttribute ('style', 'visibility:hidden;');
		var popupDiv = document.createElement('div');
		popupDiv.id = rowId +'.popupDiv';
		popupDiv.setAttribute ('style', 'visibility:hidden;display:block');
		popupDiv.setAttribute ('rowid', rowId);
		cellPopupDiv.appendChild(popupDiv);
	});

}


function add_UpdateRow(){
	var tableName = document.getElementById('tableName').value;
	var pageName = document.getElementById('pageName').value;
	var elTable = document.getElementById(tableName);
	
	var respFields = Promise.resolve(getTableData(tableName, pageName));
	var oper = document.getElementById('oper').value;
	respFields.then(function (tableData){
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
		
		for (var k = 1; k < tableData.length; k++) {
				
				var rowData = tableData[k];
				var row = (elTable.getElementsByTagName('tbody')[0]).insertRow(-1);
				for (var r=0; r<rowData.length; r++){
					var rowId = 'Row' + (k-1) + '.' + dbColumns[r];
					var cell = row.insertCell(-1);
					var hiddenInput = document.createElement('input');
					if (r< indexToHide){
						cell.setAttribute ('style', 'display:none;');
					} 
					hiddenInput.setAttribute ('name', rowId);
					hiddenInput.id = hiddenInput.name;
					hiddenInput.value = rowData[r];
					cell.appendChild(hiddenInput);
				}
				var popupBtn = document.createElement('button'); 
				popupBtn.type = 'button'; 
				popupBtn.setAttribute
					  ('onclick','showMoreProps(this, "' + oper.value + '")');
				popupBtn.id = rowId +
					  '.popupBtn'; 
				popupBtn.appendChild(document.createTextNode("Define More\nProperties")); 
			  popupBtn.style.resize = 'none';
			  popupBtn.setAttribute ('rowid', rowId); 
			  var cellButton = row.insertCell(-1); 
			  cellButton.appendChild(popupBtn); 
			  var cellPopupDiv = row.insertCell(-1); 
			  cellPopupDiv.setAttribute ('style', 'visibility:hidden;'); 
			  var popupDiv = document.createElement('div'); 
			  popupDiv.id = rowId +'.popupDiv';
			  popupDiv.setAttribute ('style', 'visibility:hidden;display:block'); 
			  popupDiv.setAttribute ('rowid', rowId); 
			  cellPopupDiv.appendChild(popupDiv); 
				
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
	r = Promise.resolve(getData('/fetch/libdatabase/gettabledata?tableName='+tname+"&pageName="+ pname)); 
	return r;
}

function getExtendedPageGuiData(tname,pname){
	r = Promise.resolve(getData('/fetch/libdatabase/pageguiextendedprops?tableName='+tname+"&pageName="+ pname)); 
	return r;
}

function getSelectControl (options){
	var opt = document.createElement('option');
	for (var j = 0; j < options.length; j++) {
		opt.text = options[j][1];
		opt.value = options[j][0];
	} 
	
	return opt;
}

function getSelectControlt (options, element){
	for (var key in options){
		var opt = document.createElement('option');
		opt.text = options[key][0];
		opt.value = key;
		element.appendChild(opt);
	}
}

function showMoreProps(e , op){
	e.disabled = true;
	var rowId = e.getAttribute('rowid');
	var popup = document.getElementById(rowId+'.popupDiv');
	if (!popup.contains(popup.querySelector('div[id=\'popupTitle\']')))
		fillPopup_new (popup, op);
	popup.style.visibility = 'visible';
	popup.style.display = 'inline';
	showRowById(rowId);
	disableMainForm(op);		
}

function fillPopup(p, op){
	var title = document.createElement ('div');
	title.setAttribute('id', 'popupTitle');
	title.appendChild(document.createTextNode("Define More\nProperties"));
	title.appendChild (getPopupCloseButtonForRowId(p.getAttribute('rowId'), op));
	p.appendChild(title);
	var content = document.createElement('div');
	content.appendChild(document.createTextNode("Extend to class:"));
	var sel = document.createElement('select');
	var rowId = p.getAttribute('rowId');
	sel.name = rowId + '.MAPPEDCLASS';
	sel.setAttribute('rowId',rowId);
	sel.setAttribute ('onchange','refreshPopup(document.getElementById("'+p.id +'"), this, this.value)');
	content.appendChild(sel);
	Promise.resolve(getData('/fetch/libdatabase/getextendedproptypes'))
	.then(function(resp){
		/*
		 * var title = document.createElement ('div'); title.setAttribute('id',
		 * 'popupTitle'); title.appendChild(document.createTextNode("Define
		 * More\nProperties")); title.appendChild
		 * (getPopupCloseButtonForRowId(p.getAttribute('rowId'), op));
		 * p.appendChild(title); var content = document.createElement('div');
		 * content.appendChild(document.createTextNode("Extend to class:")); var
		 * sel = document.createElement('select'); sel.name =
		 * p.getAttribute('rowId') + '.MAPPEDCLASS';
		 */
		if (propertyMap ===null){
			propertyMap = resp;
		}
		getSelectControlt(resp, sel);
		var exPropsTable = document.createElement('table');
		exPropsTable.appendChild(document.createElement('tbody'));
		exPropsTable.setAttribute("id", p.getAttribute('rowId')+'.exPropsTable');
		content.appendChild(exPropsTable);
		
		/* refreshPopup (exPropsTable, sel, sel.value ); */
		
		var selKey = sel.options[sel.selectedIndex].value;
		var propsJson = JSON.parse(propertyMap[selKey][1]);
		
		p.appendChild(content);
		addRowToPopup (p, propsJson);
	})
	.catch (function(error){
		
	});
	if (op === 'update'){
		var tableName = document.getElementById('tableName').value;
		var pageName = document.getElementById('pageName').value;
		Promise.resolve(getData(getExtendedPageGuiData (tableName, pageName)))
		.then(function(data){
			
		});
	}
	
	
	
	
}

function showRowById (id){
	var row = document.getElementById(id);
	row.style.visibility = 'visible';

}

function disableMainForm (op){
	disableMainFormButtions(op);
	var form = document.getElementById('formMainDiv');
	form.style.visibility = 'hidden';
}

function disableMainFormButtions(op){
	if (op === 'new'){
		document.getElementById('addRow').disabled = true;
	}
	document.getElementById('submit').disabled = true;
}

function getPopupCloseButtonForRowId (rowId, op) {
	var divCloseX = document.createElement('button');
	divCloseX.type = 'button';
	divCloseX.setAttribute ('rowid', rowId);
	divCloseX.setAttribute ('style','background: none; border:none; color:red; cursor:pointer;');
	divCloseX.setAttribute ('onclick','closeMoreProps(this, "'+op+'")');
	divCloseX.appendChild(document.createTextNode("X"));
	return divCloseX;
}

function closeMoreProps(e, op){
	var id = e.getAttribute('rowid');
	var idPopup = id+'.popupDiv';
	document.getElementById(id+'.popupBtn').disabled = false;
	enableMainForm(op);
	resetRowById(id);
	var form = document.getElementById('formMainDiv');
	form.style.visibility = 'inherit';
	var row = document.getElementById(id);
	var popup = document.getElementById(idPopup);
	row.style.visibility = 'inherit';
	popup.style.visibility = 'hidden';
	popup.style.display = 'none';
}

function enableMainForm (oper){
	enableMainFormButtions(oper);
	var form = document.getElementById('formMainDiv');
	form.style.visibility = 'inherit';
}
function enableMainFormButtions(op){
	
	if (op === 'new'){
		document.getElementById('addRow').disabled = false;
	}
	document.getElementById('submit').disabled = false;
}

function resetRowById (id){
	var row = document.getElementById(id);
	row.style.visibility = 'inherit';

}

function sleep(ms) {
	  return new Promise(resolve => setTimeout(resolve, ms));
}

function addRowToPopup(popup,rowContent){
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

function refreshPopup (popup, val){

	var rowId= popup.getAttribute('rowId');
	var content = document.getElementById(rowId+'.contentDiv');
	var tableId = rowId+'.exPropsTable.'+ val;
	var displayTable = document.getElementById(tableId);
	displayTable.disabled=false;
	displayTable.style.visibility = 'inherit';
	displayTable.style.display = 'block';
	var tables = content.getElementsByTagName('table');
	for (var t of tables){
		if (t.id=== tableId) continue ;
		t.disabled=true;
		t.style.visibility = 'hidden';
		t.style.display = 'none';
	}
	
}
	
	
	
function fillPopup_new(p, op){
	var rowId = p.getAttribute('rowId');
	var title = document.createElement ('div');
	title.setAttribute('id', 'popupTitle');
	title.appendChild(document.createTextNode("Define More\nProperties"));
	title.appendChild (getPopupCloseButtonForRowId(rowId, op));
	p.appendChild(title);
	var content = document.createElement('div');
	content.setAttribute ('id', rowId+'.contentDiv');
	content.appendChild(document.createTextNode("Extend to class:"));
	var sel = document.createElement('select');
	
	sel.name = rowId + '.MAPPEDCLASS';
	sel.setAttribute('rowId',rowId);
	sel.setAttribute ('onchange','refreshPopup(document.getElementById("'+p.id +'"), this.value)');
	content.appendChild(sel);
	p.appendChild(content);
	Promise.resolve(getData('/fetch/libdatabase/getextendedproptypes'))
	.then(function(resp){
		if (propertyMap ===null){
			propertyMap = resp;
		}
		getSelectControlt(resp, sel);
		
		for (var k of sel.options){
			var tableData = propertyMap[k.value];
			addTableToPopup (p, tableData[0], JSON.parse(tableData[1]));
		}
		
		refreshPopup (p,sel.value);
		
		
		
	})
	.catch (function(error){
		
	});
	if (op === 'update'){
		var tableName = document.getElementById('tableName').value;
		var pageName = document.getElementById('pageName').value;
		Promise.resolve(getData(getExtendedPageGuiData (tableName, pageName)))
		.then(function(data){
			
		});
	}
	
	
	
	
}

function addTableToPopup(popup,tbit, rowContent){

	var rowId = popup.getAttribute('rowId');
	var content = document.getElementById(rowId+'.contentDiv');
	var tableId = rowId+'.exPropsTable.'+ tbit ;
	var exPropsTable = document.createElement('table');
	exPropsTable.appendChild(document.createElement('tbody'));
	exPropsTable.setAttribute("id", tableId);
	content.appendChild(exPropsTable);
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
	exPropsTable.disabled=true;
	exPropsTable.style.visibility = 'hidden';
	exPropsTable.style.display = 'none';
	
	console.log ('was: existingRowCount:' + existingRowCount);
	console.log ('contentIndex:' + contentIndex);
	console.log ('new:'+ pTable.rows.length);
	
	
}
/*
function addTableToPopup (popup,tData){
	var propsJson = tData[1];
	var tableType = tData[0];
	addTableToPopup (popup, tableType, propsJson);
}
*/

