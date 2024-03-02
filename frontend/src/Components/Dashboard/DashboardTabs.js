import React, { useState } from 'react';

const DashboardTabs = () => {
  const [activeTab, setActiveTab] = useState('tab1');

  return (
    <div>
      <ul className="flex border-b">
        <li className="-mb-px mr-1">
          <button
            className={`bg-white inline-block border-l border-t border-r rounded-t py-2 px-4 text-blue-500 font-semibold ${
              activeTab === 'tab1' ? 'active' : ''
            }`}
            onClick={() => setActiveTab('tab1')}
          >
            SHC
          </button>
        </li>
      </ul>
      <div className="p-4">
        {activeTab === 'tab1' && <div>...</div>}
      </div>
    </div>
  );
};

export default DashboardTabs;
