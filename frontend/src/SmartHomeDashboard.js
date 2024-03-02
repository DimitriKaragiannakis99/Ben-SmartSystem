import React from 'react';
import Profile from './Components/Dashboard/Profile';
import HouseView from './Components/Dashboard/HouseView';
import DashboardTabs from './Components/Dashboard/DashboardTabs';


const SmartHomeDashboard = () => {
  return (
    <div className='flex h-screen'>
        <div className='w-1/4'>
            <DashboardTabs />
        </div>
        <div className='w-0.5 bg-black'></div>
        <div className='w-1/2'>
            <HouseView />
        </div>
        <div className='w-0.5 bg-black'></div>
        <div className='w-1/4 items-center justify-center'>
            <Profile />
        </div>
    </div>
  );
};

export default SmartHomeDashboard;