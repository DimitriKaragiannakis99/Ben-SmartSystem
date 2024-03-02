import React from 'react';
import Profile from './Components/Dashboard/Profile';


const SmartHomeDashboard = () => {
  return (
    <div className='flex h-screen'>
        <div className='w-1/3'>
        </div>
        <div className='w-1/2'>
        </div>
        <div className='w-1/3 items-center justify-center'>
            <Profile />
        </div>
    </div>
  );
};

export default SmartHomeDashboard;